package edu.fcu.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

class RowPackingCheckerTest {
    @Test
    void testSumRowLength() {
        List<Double> lengths = Arrays.asList(1.0, 2.0, 3.0);
        assertEquals(6.0, RowPackingChecker.sumRowLength(lengths));
    }
    @Test
    void testExceeds() {
        assertTrue(RowPackingChecker.exceeds(10.0, 9.0));
        assertFalse(RowPackingChecker.exceeds(8.0, 9.0));
    }
    @Test
    void testCanFitRowPacking() {
        // 測試可放入（itemWidth < usableWidth）
        double usableLength = 4.2 * 0.9;
        double usableWidth = 1.8 * 0.9;
        double itemLength = 2.0 * 1.1;
        double itemWidth = 1.0 * 1.1;
        assertTrue(RowPackingChecker.canFitRowPacking(usableLength, usableWidth, itemLength, itemWidth));
        // 超過車寬
        assertFalse(RowPackingChecker.canFitRowPacking(usableLength, usableWidth, 2.0 * 1.1, 2.0 * 1.1));
    }

    @Test
    void testSumRowLengthEdgeCases() {
        // 空清單
        assertEquals(0.0, RowPackingChecker.sumRowLength(List.of()));
        // 含 null
        assertEquals(3.0, RowPackingChecker.sumRowLength(Arrays.asList(1.0, null, 2.0)));
        // 含負數
        assertEquals(1.0, RowPackingChecker.sumRowLength(Arrays.asList(-5.0, 1.0, -2.0)));
        // 極大值
        assertEquals(Double.MAX_VALUE, RowPackingChecker.sumRowLength(Arrays.asList(Double.MAX_VALUE, 0.0)));
    }

    @Test
    void testExceedsEdgeCases() {
        // threshold = 0
        assertTrue(RowPackingChecker.exceeds(1.0, 0.0));
        assertFalse(RowPackingChecker.exceeds(0.0, 0.0));
        // 負數
        assertFalse(RowPackingChecker.exceeds(-1.0, 0.0));
        assertTrue(RowPackingChecker.exceeds(0.0, -1.0));
    }

    @Test
    void testCanFitRowPackingEdgeCases() {
        // 剛好等於
        assertTrue(RowPackingChecker.canFitRowPacking(2.0, 1.0, 2.0, 1.0));
        // 超過
        assertFalse(RowPackingChecker.canFitRowPacking(2.0, 1.0, 2.1, 1.0));
        assertFalse(RowPackingChecker.canFitRowPacking(2.0, 1.0, 2.0, 1.1));
        // 負數（實作上負數會被判斷為 true，因為負數 < usableLength/usableWidth）
        assertTrue(RowPackingChecker.canFitRowPacking(2.0, 1.0, -1.0, 1.0));
        assertTrue(RowPackingChecker.canFitRowPacking(2.0, 1.0, 2.0, -1.0));
    }
}
