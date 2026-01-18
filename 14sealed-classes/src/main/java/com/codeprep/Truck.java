package com.codeprep;

public final class Truck extends Vehicle {

    private final double cargoCapacity;

    public Truck(String brand, int year, double cargoCapacity) {
        super(brand, year);
        this.cargoCapacity = cargoCapacity;
    }

    public double getCargoCapacity() {
        return cargoCapacity;
    }

    @Override
    public String getVehicleType() {
        return "Truck";
    }
}
