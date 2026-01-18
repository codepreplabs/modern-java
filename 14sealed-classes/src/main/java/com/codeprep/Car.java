package com.codeprep;

public final class Car extends Vehicle {

    private final int numberOfDoors;

    public Car(String brand, int year, int numberOfDoors) {
        super(brand, year);
        this.numberOfDoors = numberOfDoors;
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    @Override
    public String getVehicleType() {
        return "Car";
    }
}
