package edu.fcu.model;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

/**
 * 車輛物件：僅描述「名義尺寸」與車型資訊，不包含任何裝載策略。
 * 幾何安全係數、可用空間等判斷，應由各 LoadCheckStrategy 決定。
 * （責任切割：Vehicle 僅為資料結構，策略才有權決定安全係數與裝載規則）
 */
@Getter
public class Vehicle {
    private String name;
    private VehicleType type;
    private double nominalLength;
    private double nominalWidth;
    private double nominalHeight;
    private List<Item> items = new ArrayList<>();

    public Vehicle(String name, VehicleType type,
                   double nominalLength, double nominalWidth, double nominalHeight) {
        this.name = name;
        this.type = type;
        this.nominalLength = nominalLength;
        this.nominalWidth  = nominalWidth;
        this.nominalHeight = nominalHeight;
    }

    public void addItem(Item item) {
        items.add(item);
    }
}
