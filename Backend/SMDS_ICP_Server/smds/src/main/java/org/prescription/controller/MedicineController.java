package org.prescription.controller;

import org.prescription.model.Medicine;
import org.prescription.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    @Autowired
    private MedicineRepository medicineRepository;

    // Check if medicine exists
    @GetMapping("/{id}")
    public ResponseEntity<Medicine> checkMedicine(@PathVariable String id) {
        Optional<Medicine> existingMedicine = medicineRepository.findById(id);
        if (existingMedicine.isPresent()) {
            return ResponseEntity.ok(existingMedicine.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Add new medicine
    @PostMapping("/add")
    public ResponseEntity<String> addMedicine(@RequestBody Medicine medicine) {
        // Check if medicine ID already exists
        if (medicineRepository.existsById(medicine.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Medicine ID already exists");
        }

        // Check if medicine name already exists (ignoring case)
        Optional<Medicine> existingMedicineByName = medicineRepository.findByNameIgnoreCase(medicine.getName());
        if (existingMedicineByName.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Medicine with the same name already exists");
        }

        // Save new medicine to the database
        medicineRepository.save(medicine);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Medicine added successfully");
    }


    // Get all medicines
    @GetMapping("/all")
    public List<Medicine> getAllMedicines(
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order) {

        List<Medicine> medicines = medicineRepository.findAll();

        if (sortBy.equalsIgnoreCase("name")) {
            medicines.sort((a, b) -> {
                return order.equalsIgnoreCase("desc")
                        ? b.getName().compareToIgnoreCase(a.getName())
                        : a.getName().compareToIgnoreCase(b.getName());
            });
        } else {
            medicines.sort((a, b) -> {
                return order.equalsIgnoreCase("desc")
                        ? b.getId().compareToIgnoreCase(a.getId())
                        : a.getId().compareToIgnoreCase(b.getId());
            });
        }

        return medicines;
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMedicine(@PathVariable String id) {
        if (medicineRepository.existsById(id)) {
            medicineRepository.deleteById(id);
            return ResponseEntity.ok("Deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medicine not found");
        }
    }
}
