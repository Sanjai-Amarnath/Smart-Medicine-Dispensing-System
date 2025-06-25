package org.prescription.repository;

import org.prescription.model.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {
    Optional<Prescription> findByPrescriptionNumber(long prescriptionNumber);  // This is for querying by the prescription number
}