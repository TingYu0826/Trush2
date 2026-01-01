package edu.fcu.model;

import java.util.List;

/**
 * 抓斗車 Assignment，僅有 points
 */
public class GrabTruckAssignment extends VehicleAssignment {
    private final int usedPoints;
    private final int maxPoints;

    public GrabTruckAssignment(VehicleType vehicleType, int vehicleNumber, List<Item> items, int usedPoints, int maxPoints) {
        super(vehicleType, vehicleNumber, items);
        this.usedPoints = usedPoints;
        this.maxPoints = maxPoints;
    }

    public int getUsedPoints() { return usedPoints; }
    public int getMaxPoints() { return maxPoints; }
}

