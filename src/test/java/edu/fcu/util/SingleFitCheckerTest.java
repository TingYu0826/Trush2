package edu.fcu.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SingleFitCheckerTest {
    @Test
    void testCanFit() {
        // 測試可放入（itemWidth < carW）
        double carL = 4.2 * 0.9;
        double carW = 1.8 * 0.9;
        double carH = 1.8 * 0.9;
        double itemL = 2.0 * 1.1;
        double itemW = 1.0 * 1.1;
        double itemH = 0.3 * 1.1;
        assertTrue(SingleFitChecker.canFit(itemL, itemW, itemH, carL, carW, carH));
        // 超過車寬
        assertFalse(SingleFitChecker.canFit(2.0 * 1.1, 2.0 * 1.1, 0.3 * 1.1, carL, carW, carH));
    }

    @Test
    void testCanFitEdgeCases() {
        double carL = 4.2 * 0.9;
        double carW = 1.8 * 0.9;
        double carH = 1.8 * 0.9;
        // 剛好等於
        assertTrue(SingleFitChecker.canFit(carL, carW, carH, carL, carW, carH));
        // 負數（實作上負數會被判斷為 true，因為負數 < 車廂尺寸）
        assertTrue(SingleFitChecker.canFit(-1.0, 1.0, 1.0, carL, carW, carH));
        assertTrue(SingleFitChecker.canFit(1.0, -1.0, 1.0, carL, carW, carH));
        assertTrue(SingleFitChecker.canFit(1.0, 1.0, -1.0, carL, carW, carH));
        // 極大值
        assertFalse(SingleFitChecker.canFit(Double.MAX_VALUE, 1.0, 1.0, carL, carW, carH));
        // 所有旋轉方向都超過
        assertFalse(SingleFitChecker.canFit(10.0, 10.0, 10.0, carL, carW, carH));
    }
}
