package edu.fcu.model;

/**
 * 內部演算法用的物品物件。
 * 新增 isDamaged 屬性，支援抓斗車工作量模型。
 */
public class Item {
    private double length;
    private double width;
    private double height;
    private String category;
    private boolean damaged; // 是否破損，可壓縮

    public Item(double length, double width, double height, String category) {
        this(length, width, height, category, false);
    }

    public Item(double length, double width, double height, String category, boolean damaged) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.category = category;
        this.damaged = damaged;
    }

    // Getter
    public double getLength() { return length; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public String getCategory() { return category; }
    public boolean isDamaged() { return damaged; }
}
