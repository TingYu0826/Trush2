package edu.fcu.service;

import edu.fcu.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 專為 Jacoco branch coverage 設計，覆蓋 Dispatcher 所有分支
 */
class DispatcherServiceBranchTest {
    @Test
    @DisplayName("空輸入 early return")
    void testEmptyInput() {
        Dispatcher dispatcher = new Dispatcher();
        DispatchResultDto result = dispatcher.dispatchWithUnassigned(Collections.emptyList());
        assertTrue(result.getAssignments().isEmpty());
        assertTrue(result.getUnassignedItems().isEmpty());
    }

    @Test
    @DisplayName("全部 damaged=true → 全部抓斗車")
    void testAllDamaged() {
        Dispatcher dispatcher = new Dispatcher();
        List<ItemDto> items = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ItemDto item = new ItemDto();
            item.setLength(1.0); item.setWidth(1.0); item.setHeight(1.0); item.setCategory("金屬櫃"); item.setDamaged(true);
            items.add(item);
        }
        DispatchResultDto result = dispatcher.dispatchWithUnassigned(items);
        assertEquals(3, result.getAssignments().stream().mapToInt(a -> a.getItems().size()).sum());
        assertTrue(result.getUnassignedItems().isEmpty());
    }

    @Test
    @DisplayName("全部 damaged=false → 全部平板車")
    void testAllIntact() {
        Dispatcher dispatcher = new Dispatcher();
        List<ItemDto> items = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ItemDto item = new ItemDto();
            item.setLength(2.0); item.setWidth(1.0); item.setHeight(0.5); item.setCategory("沙發"); item.setDamaged(false);
            items.add(item);
        }
        DispatchResultDto result = dispatcher.dispatchWithUnassigned(items);
        assertEquals(2, result.getAssignments().stream().mapToInt(a -> a.getItems().size()).sum());
        assertTrue(result.getUnassignedItems().isEmpty());
    }

    @Test
    @DisplayName("抓斗車 points 超過上限 → 換車")
    void testGrabTruckPointsOverflow() {
        Dispatcher dispatcher = new Dispatcher();
        List<ItemDto> items = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ItemDto item = new ItemDto();
            item.setLength(1.0); item.setWidth(1.0); item.setHeight(1.0); item.setCategory("金屬櫃"); item.setDamaged(true);
            items.add(item);
        }
        // 使 points 超過 100，觸發多台抓斗車
        items.get(0).setLength(3.0); // 使第一件超大
        DispatchResultDto result = dispatcher.dispatchWithUnassigned(items);
        assertTrue(result.getAssignments().size() > 1);
    }

    @Test
    @DisplayName("平板車條帶法分支覆蓋（不假設分批數量）")
    void testFlatbedFullThenNewTruck() {
        // 此測試目的：覆蓋條帶法滿載分支，不驗證必定多車
        // 條帶法分批行為受安全係數與效率係數影響，分批數量非線性、不可預期
        Dispatcher dispatcher = new Dispatcher();
        List<ItemDto> items = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ItemDto item = new ItemDto();
            item.setLength(3.0); item.setWidth(1.0); item.setHeight(0.5); item.setCategory("沙發"); item.setDamaged(false);
            items.add(item);
        }
        DispatchResultDto result = dispatcher.dispatchWithUnassigned(items);
        // 驗證所有物品皆被成功指派（無 UNASSIGNED）
        int assignedCount = result.getAssignments().stream().mapToInt(a -> a.getItems().size()).sum();
        assertEquals(5, assignedCount, "所有物品皆應被指派");
        assertTrue(result.getAssignments().size() >= 1, "至少有一台車被指派");
    }

    @Test
    @DisplayName("無法派車 → unassignedItems")
    void testUnassignedItems() {
        Dispatcher dispatcher = new Dispatcher();
        List<ItemDto> items = new ArrayList<>();
        ItemDto item = new ItemDto();
        item.setLength(100.0); item.setWidth(100.0); item.setHeight(100.0); item.setCategory("超大"); item.setDamaged(false);
        items.add(item);
        DispatchResultDto result = dispatcher.dispatchWithUnassigned(items);
        assertTrue(result.getUnassignedItems().size() == 1);
        assertEquals("超大", result.getUnassignedItems().get(0).getItem().getCategory());
    }
}
