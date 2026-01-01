package edu.fcu.util;

import edu.fcu.model.Item;

/**
 * 抓斗車專用：將物品轉換為工作量點數（Capacity Points）。
 * points 並非實際體積，而是清運工作量的估算。
 */
public class GrabTruckPointCalculator {
    // 每台抓斗車最大點數
    public static final int MAX_POINTS_PER_GRAB_TRUCK = 100;
    // 體積轉點數的比例尺
    private static final double SCALE_FACTOR = 20.0;

    /**
     * 計算單一物品的工作量點數
     * @param item 物品
     * @return points
     */
    public static int calcPoints(Item item) {
        double volume = item.getLength() * item.getWidth() * item.getHeight();
        double basePoints = volume * SCALE_FACTOR;
        double factor = 1.0;
        String cat = item.getCategory();
        if (cat.contains("床墊") || cat.contains("沙發")) {
            factor *= 1.2;
        }
        if (cat.contains("金屬")) {
            factor *= 1.3;
        }
        // 破損可壓縮
        if (item.isDamaged()) {
            factor *= 0.9;
        }
        return (int)Math.ceil(basePoints * factor);
    }
}

