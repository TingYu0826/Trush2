package edu.fcu.util;

import java.util.List;

/**
 * 條帶法裝載檢查工具。
 * 計算沿車長方向的累積長度。
 */
public class RowPackingChecker {
    /**
     * 計算所有物品的沿車長累積長度
     *
     * @param lengths 每件物品的 L*
     * @return ΣL*
     */
    public static double sumRowLength(final List<Double> lengths) {
        double sum = 0.0;

        for (final Double len : lengths) {
            if (len != null && len > 0) {
                sum += len;
            }
        }
        return sum;
    }

    /**
     * 判斷是否超過車廂門檻
     *
     * @param totalLength ΣL*
     * @param threshold 車廂門檻（例如 η × Lc）
     * @return 是否超載
     */
    public static boolean exceeds(final double totalLength, final double threshold) {
        return totalLength > threshold;
    }

    /**
     * 判斷單件物品是否可放入條帶（只檢查長寬）
     */
    public static boolean canFitRowPacking(final double usableLength, final double usableWidth, final double itemLength, final double itemWidth) {
        return itemLength <= usableLength && itemWidth <= usableWidth;
    }
}