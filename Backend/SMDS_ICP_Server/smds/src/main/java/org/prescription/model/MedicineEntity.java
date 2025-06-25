package org.prescription.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "medicine")  // Collection in MongoDB
public class MedicineEntity {

    private String id;  // MongoDB will automatically generate an _id field if you use String or ObjectId
    private String name;
    private int quantity;

    // Getters and Setters

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
