package edu.fcu.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 物品擺放方向輔助工具。
 * 提供所有可能的旋轉組合 (L, W, H)。
 */
public class OrientationHelper {

    /**
     * 回傳物品的所有可能旋轉方向（共 6 種）
     *
     * @param l 原始長
     * @param w 原始寬
     * @param h 原始高
     * @return 所有 (L, W, H) 排列組合
     */
    public static List<double[]> getAllOrientations(double l, double w, double h) {
        List<double[]> orientations = new ArrayList<>();

        orientations.add(new double[]{l, w, h}); // 原始
        orientations.add(new double[]{l, h, w}); // 長-高-寬
        orientations.add(new double[]{w, l, h}); // 寬-長-高
        orientations.add(new double[]{w, h, l}); // 寬-高-長
        orientations.add(new double[]{h, l, w}); // 高-長-寬
        orientations.add(new double[]{h, w, l}); // 高-寬-長

        return orientations;
    }
}
