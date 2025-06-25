package org.prescription.model;

public class PrescribedMedicine {
    private String name;
    private String id;
    private int quantity;

    public PrescribedMedicine(String id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}