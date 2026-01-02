package edu.fcu.service;

import edu.fcu.model.*;
import edu.fcu.strategy.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 派車模組（台灣實務版）：
 * - 僅使用「抓斗車」與「平板車」
 * - 抓斗車：政府清潔隊，處理可抓取、允許破壞之大型垃圾
 * - 平板車：民間清運，人工搬運＋堆疊（條帶法）
 */
@Service
public class Dispatcher {

    /* =========================
     * 車輛基本設定（示意值）
     * ========================= */

    // 平板車（3.5 噸 / 5 噸框式貨車）
    private static final double FLATBED_L = 4.2;
    private static final double FLATBED_W = 1.8;
    private static final double FLATBED_H = 1.8;

    // 抓斗車（非車廂模型，尺寸僅作識別）
    private static final double GRAB_L = 0;
    private static final double GRAB_W = 0;
    private static final double GRAB_H = 0;

    /* =========================
     * 車型對應策略
     * ========================= */

    private final Map<VehicleType, LoadCheckStrategy> strategyMap = new HashMap<>();

    public Dispatcher() {
        strategyMap.put(VehicleType.GRAB_TRUCK, new GrabTruckLoadCheckStrategy());
        strategyMap.put(VehicleType.FLATBED_TRUCK, new FlatbedLoadCheckStrategy());
    }

    /**
     * 派車主流程：
     * 1. DTO → Item
     * 2. 先嘗試抓斗車（逐件）
     * 3. 剩餘物品再用平板車（可多車）
     * @param itemDtos 物品 DTO 清單
     * @return 派車分配結果
     */
    public List<VehicleAssignment> dispatch(final List<ItemDto> itemDtos) {
        // 依 prompt.md，僅以 damaged 分流
        List<Item> items = itemDtos.stream()
                .map(dto -> new Item(
                        dto.getLength(),
                        dto.getWidth(),
                        dto.getHeight(),
                        dto.getCategory(),
                        dto.isDamaged()
                ))
                .collect(Collectors.toList());

        List<Item> damagedItems = new ArrayList<>();
        List<Item> intactItems = new ArrayList<>();
        for (Item item : items) {
            if (item.isDamaged()) {
                damagedItems.add(item);
            } else {
                intactItems.add(item);
            }
        }
        List<VehicleAssignment> results = new ArrayList<>();
        // 抓斗車分批（points）
        LoadCheckStrategy grabStrategy = strategyMap.get(VehicleType.GRAB_TRUCK);
        int grabIndex = 1;
        List<Item> grabQueue = new ArrayList<>(damagedItems);
        while (!grabQueue.isEmpty()) {
            Vehicle grab = createVehicle(VehicleType.GRAB_TRUCK, grabIndex);
            GrabTruckAssignment va = (GrabTruckAssignment) grabStrategy.checkLoad(grab, grabQueue);
            if (va.getItems().isEmpty()) break;
            results.add(new GrabTruckAssignment(
                    grab.getType(), grabIndex, va.getItems(), va.getUsedPoints(), va.getMaxPoints()
            ));
            grabQueue.removeAll(va.getItems());
            grabIndex++;
        }
        // 平板車分批（條帶法）
        LoadCheckStrategy flatbedStrategy = strategyMap.get(VehicleType.FLATBED_TRUCK);
        int flatbedIndex = 1;
        List<Item> flatbedQueue = new ArrayList<>(intactItems);
        while (!flatbedQueue.isEmpty()) {
            Vehicle flatbed = createVehicle(VehicleType.FLATBED_TRUCK, flatbedIndex);
            FlatbedAssignment va = (FlatbedAssignment) flatbedStrategy.checkLoad(flatbed, flatbedQueue);
            if (va.getItems().isEmpty()) break;
            results.add(new FlatbedAssignment(
                    flatbed.getType(), flatbedIndex, va.getItems(), va.getTotalLength(), va.getLoadRate()
            ));
            flatbedQueue.removeAll(va.getItems());
            flatbedIndex++;
        }
        return results;
    }

    /**
     * 派車主流程（部分成功也要回傳 unassignedItems）
     * 1. DTO → Item
     * 2. 先嘗試抓斗車（逐件）
     * 3. 剩餘物品再用平板車（可多車）
     * 4. 若有無法派車，記錄於 unassignedItems
     * @param itemDtos 物品 DTO 清單
     * @return 派車分配結果（含未分配物品）
     */
    public DispatchResultDto dispatchWithUnassigned(final List<ItemDto> itemDtos) {
        List<Item> items = itemDtos.stream()
                .map(dto -> new Item(
                        dto.getLength(),
                        dto.getWidth(),
                        dto.getHeight(),
                        dto.getCategory(),
                        dto.isDamaged()
                ))
                .collect(Collectors.toList());

        List<Item> damagedItems = new ArrayList<>();
        List<Item> intactItems = new ArrayList<>();
        List<UnassignedItemDto> unassigned = new ArrayList<>();
        // 驗證資料正確性：長寬高>0且類別不為空
        for (Item item : items) {
            if (item.getLength() <= 0 || item.getWidth() <= 0 || item.getHeight() <= 0 || item.getCategory() == null || item.getCategory().trim().isEmpty()) {
                unassigned.add(new UnassignedItemDto(toDto(item), "INVALID_INPUT"));
            } else if (item.isDamaged()) {
                damagedItems.add(item);
            } else {
                intactItems.add(item);
            }
        }
        List<VehicleAssignment> results = new ArrayList<>();
        // 抓斗車分批（points）
        LoadCheckStrategy grabStrategy = strategyMap.get(VehicleType.GRAB_TRUCK);
        int grabIndex = 1;
        List<Item> grabQueue = new ArrayList<>(damagedItems);
        while (!grabQueue.isEmpty()) {
            Vehicle grab = createVehicle(VehicleType.GRAB_TRUCK, grabIndex);
            GrabTruckAssignment va = (GrabTruckAssignment) grabStrategy.checkLoad(grab, grabQueue);
            if (va.getItems().isEmpty()) {
                // 將剩餘無法派車的 damaged 物件記錄
                for (Item item : grabQueue) {
                    unassigned.add(new UnassignedItemDto(
                            toDto(item), "CATEGORY_NOT_ALLOWED_OR_SIZE_EXCEED"));
                }
                break;
            }
            results.add(new GrabTruckAssignment(
                    grab.getType(), grabIndex, va.getItems(), va.getUsedPoints(), va.getMaxPoints()
            ));
            grabQueue.removeAll(va.getItems());
            grabIndex++;
        }
        // 平板車分批（條帶法）
        LoadCheckStrategy flatbedStrategy = strategyMap.get(VehicleType.FLATBED_TRUCK);
        int flatbedIndex = 1;
        List<Item> flatbedQueue = new ArrayList<>(intactItems);
        while (!flatbedQueue.isEmpty()) {
            Vehicle flatbed = createVehicle(VehicleType.FLATBED_TRUCK, flatbedIndex);
            FlatbedAssignment va = (FlatbedAssignment) flatbedStrategy.checkLoad(flatbed, flatbedQueue);
            if (va.getItems().isEmpty()) {
                // 將剩餘無法派車的 intact 物件記錄
                for (Item item : flatbedQueue) {
                    String reason = getFlatbedFailReason(item, flatbed);
                    unassigned.add(new UnassignedItemDto(toDto(item), reason));
                }
                break;
            }
            results.add(new FlatbedAssignment(
                    flatbed.getType(), flatbedIndex, va.getItems(), va.getTotalLength(), va.getLoadRate()
            ));
            flatbedQueue.removeAll(va.getItems());
            flatbedIndex++;
        }
        return new DispatchResultDto(results, unassigned);
    }

    /**
     * 建立車輛物件
     */
    private Vehicle createVehicle(VehicleType type, int num) {
        switch (type) {
            case GRAB_TRUCK:
                return new Vehicle(
                        "抓斗車-" + num,
                        type,
                        GRAB_L,
                        GRAB_W,
                        GRAB_H
                );
            case FLATBED_TRUCK:
                return new Vehicle(
                        "平板車-" + num,
                        type,
                        FLATBED_L,
                        FLATBED_W,
                        FLATBED_H
                );
            default:
                throw new IllegalArgumentException("Unsupported vehicle type");
        }
    }

    /**
     * 將 Item 轉回 ItemDto
     */
    private ItemDto toDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setLength(item.getLength());
        dto.setWidth(item.getWidth());
        dto.setHeight(item.getHeight());
        dto.setCategory(item.getCategory());
        dto.setDamaged(item.isDamaged());
        return dto;
    }

    /**
     * 判斷平板車無法派車原因
     * 注意：Vehicle 僅提供名義尺寸，幾何安全係數由策略決定。
     */
    private String getFlatbedFailReason(Item item, Vehicle flatbed) {
        // 以名義尺寸判斷（不含安全係數，僅作失敗原因提示）
        if (item.getHeight() > flatbed.getNominalHeight()) return "HEIGHT_EXCEED";
        if (item.getLength() > flatbed.getNominalLength() || item.getWidth() > flatbed.getNominalWidth()) return "SIZE_EXCEED";
        return "UNKNOWN";
    }
}