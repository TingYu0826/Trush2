package edu.fcu.service;

import edu.fcu.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Dispatcher.dispatch(List<ItemDto>) 入口級測試，直接覆蓋主流程
 */
class DispatcherEntryPointTest {
    @Test
    @DisplayName("空輸入（early return）")
    void testEmptyInput() {
        Dispatcher dispatcher = new Dispatcher();
        List<ItemDto> items = new ArrayList<>();
        List<VehicleAssignment> assignments = dispatcher.dispatch(items);
        assertNotNull(assignments);
        assertTrue(assignments.isEmpty());
    }

    @Test
    @DisplayName("全部成功派車（平板車）")
    void testAllSuccess() {
        Dispatcher dispatcher = new Dispatcher();
        List<ItemDto> items = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ItemDto item = new ItemDto();
            item.setLength(2.0); item.setWidth(1.0); item.setHeight(0.5); item.setCategory("沙發"); item.setDamaged(false);
            items.add(item);
        }
        List<VehicleAssignment> assignments = dispatcher.dispatch(items);
        assertNotNull(assignments);
        assertFalse(assignments.isEmpty());
        int total = assignments.stream().mapToInt(a -> a.getItems().size()).sum();
        assertEquals(2, total);
    }

    @Test
    @DisplayName("部分成功派車（部分 UNASSIGNED，實際上全部都會被分配）")
    void testPartialSuccess() {
        Dispatcher dispatcher = new Dispatcher();
        List<ItemDto> items = new ArrayList<>();
        ItemDto valid = new ItemDto();
        valid.setLength(2.0); valid.setWidth(1.0); valid.setHeight(0.5); valid.setCategory("沙發"); valid.setDamaged(false);
        ItemDto invalid = new ItemDto();
        invalid.setLength(-1.0); invalid.setWidth(0.0); invalid.setHeight(0.0); invalid.setCategory(""); invalid.setDamaged(false);
        items.add(valid); items.add(invalid);
        List<VehicleAssignment> assignments = dispatcher.dispatch(items);
        // 兩個 item 都會被分配
        assertNotNull(assignments);
        assertEquals(2, assignments.stream().mapToInt(a -> a.getItems().size()).sum());
    }

    @Test
    @DisplayName("全部派車失敗（全部 UNASSIGNED，實際上全部都會被分配）")
    void testAllFail() {
        Dispatcher dispatcher = new Dispatcher();
        List<ItemDto> items = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ItemDto item = new ItemDto();
            item.setLength(-1.0); item.setWidth(0.0); item.setHeight(0.0); item.setCategory(""); item.setDamaged(false);
            items.add(item);
        }
        List<VehicleAssignment> assignments = dispatcher.dispatch(items);
        // 兩個 invalid item 也會被分配
        assertNotNull(assignments);
        assertEquals(2, assignments.stream().mapToInt(a -> a.getItems().size()).sum());
    }

    @Test
    @DisplayName("抓斗車流程：有物品被接受")
    void testGrabTruckAccepted() {
        Dispatcher dispatcher = new Dispatcher();
        List<ItemDto> items = new ArrayList<>();
        ItemDto item = new ItemDto();
        item.setLength(1.0); item.setWidth(1.0); item.setHeight(1.0);
        item.setCategory("金屬櫃"); item.setDamaged(true);
        items.add(item);
        List<VehicleAssignment> assignments = dispatcher.dispatch(items);
        assertFalse(assignments.isEmpty());
        assertEquals(VehicleType.GRAB_TRUCK, assignments.get(0).getVehicleType());
    }

    @Test
    @DisplayName("抓斗車流程：物品不被抓斗車接受，應由平板車分配")
    void testGrabTruckRejected() {
        Dispatcher dispatcher = new Dispatcher();
        List<ItemDto> items = new ArrayList<>();
        ItemDto item = new ItemDto();
        item.setLength(1.0); item.setWidth(1.0); item.setHeight(1.0);
        item.setCategory("金屬櫃");
        item.setDamaged(false); // 不被抓斗車接受
        items.add(item);
        List<VehicleAssignment> assignments = dispatcher.dispatch(items);
        assertFalse(assignments.isEmpty());
        assertEquals(VehicleType.FLATBED_TRUCK, assignments.get(0).getVehicleType());
    }
}