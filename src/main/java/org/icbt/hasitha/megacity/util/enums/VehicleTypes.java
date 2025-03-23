package org.icbt.hasitha.megacity.util.enums;

public enum VehicleTypes {
    SEDAN("Sedan"),
    SUV("SUV"),
    VAN("Van"),
    LUXURY("Luxury");

    private final String displayName;

    VehicleTypes(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

