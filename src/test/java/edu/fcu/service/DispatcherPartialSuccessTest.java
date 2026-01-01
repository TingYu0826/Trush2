package edu.fcu.service;

import edu.fcu.model.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class DispatcherPartialSuccessTest {
    @Test
    void testPartialSuccessAndUnassigned() {
        Dispatcher dispatcher = new Dispatcher();
        List<ItemDto> items = new ArrayList<>();
        // 可派平板車
        ItemDto sofa = new ItemDto();
        sofa.setLength(2.0); sofa.setWidth(0.8); sofa.setHeight(0.7); sofa.setCategory("沙發"); sofa.setDamaged(false);
        // 可派抓斗車
        ItemDto fridge = new ItemDto();
        fridge.setLength(1.2); fridge.setWidth(0.7); fridge.setHeight(1.8); fridge.setCategory("冰箱"); fridge.setDamaged(true);
        // 派不出去（超大底面）
        ItemDto big = new ItemDto();
        big.setLength(10.0); big.setWidth(10.0); big.setHeight(1.0); big.setCategory("超大物件"); big.setDamaged(false);
        items.add(sofa); items.add(fridge); items.add(big);
        DispatchResultDto result = dispatcher.dispatchWithUnassigned(items);
        assertEquals(2, result.getAssignments().stream().mapToInt(a -> a.getItems().size()).sum());
        assertEquals(1, result.getUnassignedItems().size());
        assertEquals("超大物件", result.getUnassignedItems().get(0).getItem().getCategory());
        assertEquals("SIZE_EXCEED", result.getUnassignedItems().get(0).getFailReason());
    }
}
