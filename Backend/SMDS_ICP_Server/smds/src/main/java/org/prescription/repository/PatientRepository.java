package org.prescription.repository;

import org.prescription.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PatientRepository extends MongoRepository<Patient, String> {
    Patient findByPatientId(String patientId);
}