package org.prescription.controller;

import org.prescription.model.Prescription;
import org.prescription.repository.PrescriptionRepository;
import org.prescription.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@CrossOrigin(origins = "*") // Add this above your controller
@RestController
public class PrescriptionController {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private SequenceGeneratorService sequenceGenerator;

    @PostMapping("/api/prescription")
    public ResponseEntity<String> savePrescription(@RequestBody Prescription prescription) {
        prescription.setPrescriptionNumber(sequenceGenerator.generateSequence("prescription_number_seq"));

        if (prescription.getMedicineAllocatedTime() == null || prescription.getMedicineAllocatedTime().equals("Not allocated")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String now = LocalDateTime.now().format(formatter);
            prescription.setMedicineAllocatedTime(now);
        }

        prescriptionRepository.save(prescription);
        System.out.println("Saved prescription: " + prescription);
        return ResponseEntity.ok("Prescription saved with number: " + prescription.getPrescriptionNumber());
    }

    // Get all prescriptions
    @GetMapping("/api/prescriptions")
    public ResponseEntity<?> getAllPrescriptions() {
        return ResponseEntity.ok(prescriptionRepository.findAll());
    }

    @PatchMapping("/api/prescription/payment/{prescriptionId}")
    public ResponseEntity<String> updatePayment(@PathVariable long prescriptionId) {
        System.out.println("Prescription id updated is " + prescriptionId);
        Optional<Prescription> optional = prescriptionRepository.findByPrescriptionNumber(prescriptionId);

        if (optional.isPresent()) {
            Prescription p = optional.get();
            p.setPaymentStatus(true);
            prescriptionRepository.save(p);
            return ResponseEntity.ok("Payment status updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prescription not found");
        }
    }
}