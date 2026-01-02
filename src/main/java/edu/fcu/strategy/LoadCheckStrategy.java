package edu.fcu.strategy;

import edu.fcu.model.Item;
import edu.fcu.model.Vehicle;
import edu.fcu.model.VehicleAssignment;
import java.util.List;

/**
 * 車型檢核策略介面。
 */
public interface LoadCheckStrategy {
    /**
     * 檢查指定車輛是否能裝載物品清單。
     * @param vehicle 車輛
     * @param items 物品清單
     * @return 裝載分配結果
     */
    VehicleAssignment checkLoad(Vehicle vehicle, List<Item> items);
    /**
     * 判斷單一物品是否可由此車型裝載。
     * @param item 物品
     * @return 是否可裝載
     */
    boolean canAccept(Item item);
}
