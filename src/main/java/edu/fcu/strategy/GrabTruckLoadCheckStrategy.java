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
    /**
     * 依 points 模型分批裝載。
     * @param vehicle 車輛
     * @param items 物品清單
     * @return 裝載分配結果
     */
    @Override
    public GrabTruckAssignment checkLoad(final Vehicle vehicle, final List<Item> items) {
        final List<Item> loaded = new ArrayList<>();
        int currentPoints = 0;
        for (final Item item : items) {
            // 僅接受 damaged==true
            if (!item.isDamaged()) break;
            final int itemPoints = GrabTruckPointCalculator.calcPoints(item);
            if (currentPoints + itemPoints > GrabTruckPointCalculator.MAX_POINTS_PER_GRAB_TRUCK) {
                break;
            }
            loaded.add(item);
            currentPoints += itemPoints;
        }
        return new GrabTruckAssignment(vehicle.getType(), 0, loaded, currentPoints, GrabTruckPointCalculator.MAX_POINTS_PER_GRAB_TRUCK);
    }

    /**
     * 僅接受 damaged==true
     * @param item 物品
     * @return 是否可裝載
     */
    @Override
    public boolean canAccept(final Item item) {
        // 僅接受 damaged==true
        return item != null && item.isDamaged();
    }
}
