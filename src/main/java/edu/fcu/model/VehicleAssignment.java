package edu.fcu.model;

import java.util.List;

/**
 * 派車分配結果（抽象父類，不含裝載率）
 */
public abstract class VehicleAssignment {
    private final VehicleType vehicleType;
    private final int vehicleNumber;
    private final List<Item> items;

    public VehicleAssignment(VehicleType vehicleType, int vehicleNumber, List<Item> items) {
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
        this.items = items;
    }

    public VehicleType getVehicleType() { return vehicleType; }
    public int getVehicleNumber() { return vehicleNumber; }
    public List<Item> getItems() { return items; }
}

