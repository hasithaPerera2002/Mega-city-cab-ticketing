package org.icbt.hasitha.megacity.util.enums;

public enum VehicleTypesSeatingCapacity {
    SEDAN(5),
    SUV(7),
    VAN(12);

    private final int seatingCapacity;

    VehicleTypesSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }
}
