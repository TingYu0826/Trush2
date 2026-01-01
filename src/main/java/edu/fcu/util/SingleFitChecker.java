package edu.fcu.util;

import java.util.List;

/**
 * 單一物品裝載檢查工具。
 * 判斷物品是否能以任一旋轉方向放入車廂。
 */
public class SingleFitChecker {

    /**
     * 判斷物品是否能放入指定尺寸空間
     *
     * @param itemL 物品長
     * @param itemW 物品寬
     * @param itemH 物品高
     * @param carL 車廂長
     * @param carW 車廂寬
     * @param carH 車廂高
     * @return 是否可放入
     */
    public static boolean canFit(
            double itemL, double itemW, double itemH,
            double carL, double carW, double carH
    ) {
        // 取得所有旋轉方向
        List<double[]> orientations =
                OrientationHelper.getAllOrientations(itemL, itemW, itemH);

        for (double[] o : orientations) {
            double l = o[0];
            double w = o[1];
            double h = o[2];

            // 只要有一種方向能放入即通過
            if (l <= carL && w <= carW && h <= carH) {
                return true;
            }
        }
        return false;
    }
}
