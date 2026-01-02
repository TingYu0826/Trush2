package edu.fcu.strategy;

import edu.fcu.model.Item;
import edu.fcu.util.GrabTruckPointCalculator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GrabTruckLoadCheckStrategyTest {
    @Test
    void testCanAcceptDamagedOnly() {
        GrabTruckLoadCheckStrategy strategy = new GrabTruckLoadCheckStrategy();
        // 只接受 damaged==true
        assertTrue(strategy.canAccept(new Item(1,1,1,"冰箱",true)));
        assertTrue(strategy.canAccept(new Item(1,1,1,"金屬櫃子",true)));
        assertFalse(strategy.canAccept(new Item(1,1,1,"沙發",false)));
        assertFalse(strategy.canAccept(new Item(1,1,1,"床墊",false)));
    }

    @Test
    void testCheckLoadPointsExceed() {
        GrabTruckLoadCheckStrategy strategy = new GrabTruckLoadCheckStrategy();
        // 兩件物品，第一件已超過上限
        Item over = new Item(10,10,1,"冰箱",true); // points > 100
        Item normal = new Item(1,1,1,"冰箱",true);
        var va = strategy.checkLoad(new edu.fcu.model.Vehicle("test", edu.fcu.model.VehicleType.GRAB_TRUCK, 1,1,1), List.of(over, normal));
        // 第一件已超過上限，應無法裝載任何 item
        assertEquals(0, va.getItems().size());
    }

    @Test
    void testCheckLoadEmptyList() {
        GrabTruckLoadCheckStrategy strategy = new GrabTruckLoadCheckStrategy();
        var va = strategy.checkLoad(new edu.fcu.model.Vehicle("test", edu.fcu.model.VehicleType.GRAB_TRUCK, 1,1,1), List.of());
        assertTrue(va.getItems().isEmpty());
    }

    @Test
    void testCheckLoadFirstNotDamaged() {
        GrabTruckLoadCheckStrategy strategy = new GrabTruckLoadCheckStrategy();
        Item notDamaged = new Item(1,1,1,"冰箱",false);
        Item damaged = new Item(1,1,1,"冰箱",true);
        var va = strategy.checkLoad(new edu.fcu.model.Vehicle("test", edu.fcu.model.VehicleType.GRAB_TRUCK, 1,1,1), List.of(notDamaged, damaged));
        assertTrue(va.getItems().isEmpty());
    }

    @Test
    void testCheckLoadJustFull() {
        GrabTruckLoadCheckStrategy strategy = new GrabTruckLoadCheckStrategy();
        // 多件物品剛好累加到 100 分
        Item a = new Item(1,1,1,"冰箱",true); // 20*1*1*1*0.9 = 18
        Item b = new Item(1,1,1,"金屬櫃子",true); // 20*1*1*1*1.3*0.9 = 23.4
        Item c = new Item(2,1,1,"沙發",true); // 20*2*1*1*1.2*0.9 = 43.2
        Item d = new Item(1,1,1,"金屬櫃子",true); // 23.4
        var va = strategy.checkLoad(new edu.fcu.model.Vehicle("test", edu.fcu.model.VehicleType.GRAB_TRUCK, 1,1,1), List.of(a,b,c,d));
        assertTrue(va.getUsedPoints() <= GrabTruckPointCalculator.MAX_POINTS_PER_GRAB_TRUCK);
        assertTrue(!va.getItems().isEmpty());
    }
}
