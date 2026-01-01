package edu.fcu.strategy;

import edu.fcu.model.Item;
import edu.fcu.model.Vehicle;
import edu.fcu.model.VehicleAssignment;
import java.util.List;

/**
 * 車型檢核策略介面。
 */
public interface LoadCheckStrategy {
    VehicleAssignment checkLoad(Vehicle vehicle, List<Item> items);
    boolean canAccept(Item item);
}
