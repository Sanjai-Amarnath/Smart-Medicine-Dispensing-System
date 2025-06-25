package org.prescription.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "prescription")
public class Prescription {
    @Id
    private String id;
    private long prescriptionNumber;
    private String patientId;  // New field for patient ID
    private String patientName;
    private String phoneNumber;
    private List<PrescribedMedicine> medicines;
    private String medicineAllocatedTime = "Not allocated";
    private boolean paymentStatus = false;
    private String dispensingStatus = null;
    private String dispensingTime = "Not dispensed";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getPrescriptionNumber() {
        return prescriptionNumber;
    }

    public void setPrescriptionNumber(long prescriptionNumber) {
        this.prescriptionNumber = prescriptionNumber;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<PrescribedMedicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<PrescribedMedicine> medicines) {
        this.medicines = medicines;
    }

    public String getMedicineAllocatedTime() {
        return medicineAllocatedTime;
    }

    public void setMedicineAllocatedTime(String medicineAllocatedTime) {
        this.medicineAllocatedTime = medicineAllocatedTime;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDispensingStatus() {
        return dispensingStatus;
    }

    public void setDispensingStatus(String dispensingStatus) {
        this.dispensingStatus = dispensingStatus;
    }

    public String getDispensingTime() {
        return dispensingTime;
    }

    public void setDispensingTime(String dispensingTime) {
        this.dispensingTime = dispensingTime;
    }
}
