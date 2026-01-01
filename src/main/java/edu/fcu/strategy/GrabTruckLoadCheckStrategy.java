package edu.fcu.strategy;

import edu.fcu.model.*;
import edu.fcu.util.GrabTruckPointCalculator;
import java.util.ArrayList;
import java.util.List;

/**
 * 抓斗車派車策略（政府清潔隊專用）
 * 僅依 points 模型分批，不得引用 Vehicle 的任何尺寸欄位。
 */
public class GrabTruckLoadCheckStrategy implements LoadCheckStrategy {
    @Override
    public GrabTruckAssignment checkLoad(Vehicle vehicle, List<Item> items) {
        List<Item> loaded = new ArrayList<>();
        int currentPoints = 0;
        for (Item item : items) {
            // 僅接受 damaged==true
            if (!item.isDamaged()) break;
            int itemPoints = GrabTruckPointCalculator.calcPoints(item);
            if (currentPoints + itemPoints > GrabTruckPointCalculator.MAX_POINTS_PER_GRAB_TRUCK) {
                break;
            }
            loaded.add(item);
            currentPoints += itemPoints;
        }
        return new GrabTruckAssignment(vehicle.getType(), 0, loaded, currentPoints, GrabTruckPointCalculator.MAX_POINTS_PER_GRAB_TRUCK);
    }

    @Override
    public boolean canAccept(Item item) {
        // 僅接受 damaged==true
        return item != null && item.isDamaged();
    }
}
