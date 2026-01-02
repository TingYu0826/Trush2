package edu.fcu.service;

import edu.fcu.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 補齊 Dispatcher 邊界/異常分支覆蓋
 */
class DispatcherServiceEdgeCaseTest {
    @Test
    @DisplayName("全部 invalid 輸入 → 全部 unassigned, failReason=INVALID_INPUT")
    void testAllInvalidInput() {
        Dispatcher dispatcher = new Dispatcher();
        List<ItemDto> items = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ItemDto item = new ItemDto();
            item.setLength(-1.0); item.setWidth(0.0); item.setHeight(0.0); item.setCategory(""); item.setDamaged(false);
            items.add(item);
        }
        DispatchResultDto result = dispatcher.dispatchWithUnassigned(items);
        assertTrue(result.getAssignments().isEmpty());
        assertEquals(3, result.getUnassignedItems().size());
        assertTrue(result.getUnassignedItems().stream().allMatch(u -> "INVALID_INPUT".equals(u.getFailReason())));
    }

    @Test
    @DisplayName("部分 invalid, 部分 valid → 部分 unassigned, 部分成功")
    void testPartialInvalidInput() {
        Dispatcher dispatcher = new Dispatcher();
        List<ItemDto> items = new ArrayList<>();
        ItemDto valid = new ItemDto();
        valid.setLength(2.0); valid.setWidth(1.0); valid.setHeight(0.5); valid.setCategory("沙發"); valid.setDamaged(false);
        ItemDto invalid = new ItemDto();
        invalid.setLength(-1.0); invalid.setWidth(0.0); invalid.setHeight(0.0); invalid.setCategory(""); invalid.setDamaged(false);
        items.add(valid); items.add(invalid);
        DispatchResultDto result = dispatcher.dispatchWithUnassigned(items);
        assertEquals(1, result.getAssignments().stream().mapToInt(a -> a.getItems().size()).sum());
        assertEquals(1, result.getUnassignedItems().size());
        assertEquals("INVALID_INPUT", result.getUnassignedItems().get(0).getFailReason());
    }

    @Test
    @DisplayName("超大物件無法派 → failReason=SIZE_EXCEED")
    void testUnassignedSizeExceed() {
        Dispatcher dispatcher = new Dispatcher();
        List<ItemDto> items = new ArrayList<>();
        ItemDto tooBig = new ItemDto();
        tooBig.setLength(100.0); tooBig.setWidth(100.0); tooBig.setHeight(100.0); tooBig.setCategory("超大"); tooBig.setDamaged(false);
        items.add(tooBig);
        DispatchResultDto result = dispatcher.dispatchWithUnassigned(items);
        assertEquals(1, result.getUnassignedItems().size());
        assertTrue(result.getUnassignedItems().stream().anyMatch(u -> "SIZE_EXCEED".equals(u.getFailReason()) || "HEIGHT_EXCEED".equals(u.getFailReason())));
    }
}
