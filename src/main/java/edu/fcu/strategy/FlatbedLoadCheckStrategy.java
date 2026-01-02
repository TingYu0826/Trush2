package edu.fcu.strategy;

import edu.fcu.model.*;
import edu.fcu.util.OrientationHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * 平板車 Assignment，才有裝載率
 * 幾何安全係數完全由本策略決定，Vehicle 僅提供名義尺寸。
 * - usableLength = nominalLength * 0.9
 * - usableWidth  = nominalWidth（不再打折）
 * - 高度不作為派車失敗條件（開放式車斗，家具可躺放）
 *
 * 【重要註解】
 * 台灣清潔隊平板車多為開放式載具，家具可旋轉躺放，
 * 高度不構成裝載限制，因此本策略僅檢查底面尺寸（長×寬），
 * 不檢查高度。避免如衣櫃、床墊因高度超過而誤判無法派車。
 */
public class FlatbedLoadCheckStrategy implements LoadCheckStrategy {
    private static final double ITEM_SCALE = 1.1; // 長寬放大 1.1
    private static final double EFFICIENCY = 0.85;

    /**
     * 判斷單一物品是否可裝入平板車（允許旋轉，只檢查底面尺寸，不檢查高度）
     * @param vehicle 車輛
     * @param item 物品
     * @return 是否可裝載
     */
    private boolean canFitWithRotation(final Vehicle vehicle, final Item item) {
        final double usableLength = vehicle.getNominalLength() * 0.9;
        final double usableWidth  = vehicle.getNominalWidth(); // 不再打折
        final List<double[]> orientations = OrientationHelper.getAllOrientations(
                item.getLength() * ITEM_SCALE,
                item.getWidth() * ITEM_SCALE,
                item.getHeight() * ITEM_SCALE
        );
        for (final double[] o : orientations) {
            final double baseLength = o[0];
            final double baseWidth = o[1];
            // 只檢查底面尺寸
            if (baseLength <= usableLength && baseWidth <= usableWidth) {
                return true;
            }
        }
        return false;
    }

    /**
     * 檢查物品能否裝入平板車，並回傳裝載結果。
     * 條帶法累加長度，僅以最省長度的旋轉方式計算。
     * @param vehicle 車輛
     * @param items 物品清單
     * @return 裝載分配結果
     */
    @Override
    public FlatbedAssignment checkLoad(final Vehicle vehicle, final List<Item> items) {
        final double usableLength = vehicle.getNominalLength() * 0.9;
        final double usableWidth  = vehicle.getNominalWidth(); // 不再打折
        final List<Item> loaded = new ArrayList<>();
        double usedLength = 0;
        for (final Item item : items) {
            if (!canFitWithRotation(vehicle, item)) break;
            double minL = Double.MAX_VALUE;
            final List<double[]> orientations = OrientationHelper.getAllOrientations(
                    item.getLength() * ITEM_SCALE,
                    item.getWidth() * ITEM_SCALE,
                    item.getHeight() * ITEM_SCALE
            );
            for (final double[] o : orientations) {
                final double baseLength = o[0];
                final double baseWidth = o[1];
                if (baseLength <= usableLength && baseWidth <= usableWidth) {
                    minL = Math.min(minL, baseLength);
                }
            }
            if (usedLength + minL > usableLength * EFFICIENCY) break;
            loaded.add(item);
            usedLength += minL;
        }
        final double loadRate = usedLength / (usableLength * EFFICIENCY);
        return new FlatbedAssignment(vehicle.getType(), 0, loaded, usedLength, loadRate);
    }

    /**
     * 平板車接受所有物品
     * @param item 物品
     * @return 一律回傳 true
     */
    @Override
    public boolean canAccept(final Item item) {
        return true;
    }
}