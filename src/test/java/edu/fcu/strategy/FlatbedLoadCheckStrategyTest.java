package edu.fcu.strategy;

import edu.fcu.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class FlatbedLoadCheckStrategyTest {
    @Test
    void testWardrobeCanLayDown() {
        Vehicle flatbed = new Vehicle("平板車", VehicleType.FLATBED_TRUCK, 4.3, 1.9, 2.0);
        Item wardrobe = new Item(1.8, 0.6, 2.0, "木製衣櫃", false);
        FlatbedLoadCheckStrategy strategy = new FlatbedLoadCheckStrategy();
        assertTrue(strategy.canAccept(wardrobe));
        assertTrue(strategy.checkLoad(flatbed, List.of(wardrobe)).getItems().size() == 1);
    }

    @Test
    void testMattressCanLoad() {
        Vehicle flatbed = new Vehicle("平板車", VehicleType.FLATBED_TRUCK, 4.3, 1.9, 2.0);
        Item mattress = new Item(2.0, 1.5, 0.3, "雙人床墊", false);
        FlatbedLoadCheckStrategy strategy = new FlatbedLoadCheckStrategy();
        assertTrue(strategy.canAccept(mattress));
        assertTrue(strategy.checkLoad(flatbed, List.of(mattress)).getItems().size() == 1);
    }

    @Test
    void testOversizeCannotLoad() {
        Vehicle flatbed = new Vehicle("平板車", VehicleType.FLATBED_TRUCK, 4.3, 1.9, 2.0);
        Item big = new Item(4.5, 2.0, 2.0, "超大物件", false);
        FlatbedLoadCheckStrategy strategy = new FlatbedLoadCheckStrategy();
        assertTrue(strategy.canAccept(big));
        assertTrue(strategy.checkLoad(flatbed, List.of(big)).getItems().isEmpty());
    }
}

