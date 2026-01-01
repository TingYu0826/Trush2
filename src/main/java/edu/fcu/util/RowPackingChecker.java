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
    public static double sumRowLength(List<Double> lengths) {
        double sum = 0.0;

        for (Double len : lengths) {
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
    public static boolean exceeds(double totalLength, double threshold) {
        return totalLength > threshold;
    }
}
