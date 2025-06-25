package org.prescription.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.prescription.model.Medicine;
import org.prescription.model.PrescribedMedicine;
import org.prescription.model.Prescription;
import org.prescription.repository.MedicineRepository;
import org.prescription.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/dispense")
public class DispensingController {
    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @PostMapping("/{prescriptionNumber}")
    public ResponseEntity<String> dispenseMedicine(@PathVariable long prescriptionNumber) {
        // Step 1: Find the prescription by prescription number
        Optional<Prescription> optionalPrescription = prescriptionRepository.findByPrescriptionNumber(prescriptionNumber);

        if (optionalPrescription.isEmpty() || !optionalPrescription.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medicine Dispensed");
        }

        Prescription prescription = optionalPrescription.get();

        // Step 2: Check payment status
        if (!prescription.isPaymentStatus()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Payment not completed for this prescription");
        }

        if ("Dispensed".equalsIgnoreCase(prescription.getDispensingStatus())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Already dispensed");
        }

        // Step 3: Get the medicines and quantities from the prescription
        List<PrescribedMedicine> prescribedMedicines = prescription.getMedicines();

        try {
            // Prepare the JSON payload with medicine IDs and quantities
            ObjectMapper objectMapper = new ObjectMapper();
            List<MedicineDispenseRequest> medicineDetails = prescribedMedicines.stream()
                    .map(prescribedMedicine -> {
                        Optional<Medicine> optionalMedicine = medicineRepository.findByNameIgnoreCase(prescribedMedicine.getName());
                        if (optionalMedicine.isPresent()) {
                            Medicine storedMedicine = optionalMedicine.get();
                            return new MedicineDispenseRequest(storedMedicine.getId(), prescribedMedicine.getQuantity());
                        }
                        return null;
                    })
                    .filter(medicine -> medicine != null)
                    .toList();

            String jsonData = objectMapper.writeValueAsString(medicineDetails);

            // Step 4: Send the data to the ESP8266
            String esp8266Url = "http://192.168.207.216/dispense"; // Replace with your actual ESP8266 IP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(esp8266Url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                    .build();

            HttpResponse<String> espResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("ESP Response Status: " + espResponse.statusCode());
            System.out.println("ESP Response Body: " + espResponse.body());

            // Step 5: Handle response
            if (espResponse.statusCode() == 200) { // <- temporary loose check
                prescription.setDispensingStatus("Dispensed");
                prescriptionRepository.save(prescription);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String dispensingTime = LocalDateTime.now().format(formatter);
                prescription.setDispensingTime(dispensingTime);
                prescriptionRepository.save(prescription);
                return ResponseEntity.ok("Dispensing successful and database updated");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body("ESP8266 failed to confirm dispensing: " + espResponse.body());
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error communicating with ESP8266: " + e.getMessage());
        }
    }

    // Helper class to store medicine ID and quantity
    public static class MedicineDispenseRequest {
        private String medicineId;
        private int quantity;

        public MedicineDispenseRequest(String medicineId, int quantity) {
            this.medicineId = medicineId;
            this.quantity = quantity;
        }

        public String getMedicineId() {
            return medicineId;
        }

        public void setMedicineId(String medicineId) {
            this.medicineId = medicineId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
