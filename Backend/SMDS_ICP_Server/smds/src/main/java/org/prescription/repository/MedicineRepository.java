package org.prescription.repository;

import org.prescription.model.Medicine;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MedicineRepository extends MongoRepository<Medicine, String> {
    Optional<Medicine> findById(String id);
    boolean existsById(String id);
    Optional<Medicine> findByNameIgnoreCase(String name);
}
