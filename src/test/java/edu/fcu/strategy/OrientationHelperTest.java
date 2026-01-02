package edu.fcu.strategy;

import edu.fcu.model.Item;
import edu.fcu.model.Vehicle;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OrientationHelperTest {
    @Test
    void testAllOrientationsUniqueAndCount() {
        double l = 2.0, w = 1.0, h = 0.5;
        List<double[]> orientations = edu.fcu.util.OrientationHelper.getAllOrientations(l, w, h);
        assertEquals(6, orientations.size(), "應有6種旋轉");
        // 檢查無重複
        for (int i = 0; i < orientations.size(); i++) {
            for (int j = i + 1; j < orientations.size(); j++) {
                assertFalse(java.util.Arrays.equals(orientations.get(i), orientations.get(j)), "旋轉方向不應重複");
            }
        }
    }
}

