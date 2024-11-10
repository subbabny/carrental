package com.org.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "car_inventory")
public class CarInventory {
    @Id
    private String carType;
    private int availableCount;

    private int maxCount;

    // Getters and Setters
    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public int getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(int availableCount) {
        this.availableCount = availableCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }
}
