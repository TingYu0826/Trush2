package edu.fcu.model;

import java.util.List;

/**
 * 平板車 Assignment，才有裝載率
 */
public class FlatbedAssignment extends VehicleAssignment {
    private final double loadRate;
    private final double totalLength;

    public FlatbedAssignment(VehicleType vehicleType, int vehicleNumber, List<Item> items, double totalLength, double loadRate) {
        super(vehicleType, vehicleNumber, items);
        this.totalLength = totalLength;
        this.loadRate = loadRate;
    }

    public double getLoadRate() { return loadRate; }
    public double getTotalLength() { return totalLength; }
}

