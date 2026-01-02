package edu.fcu.strategy;

import edu.fcu.model.Item;
import org.junit.jupiter.api.Test;
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
}
