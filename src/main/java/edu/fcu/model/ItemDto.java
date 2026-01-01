package edu.fcu.model;

/**
 * 前端傳入的物品資料 DTO。
 * 新增 damaged 欄位，支援抓斗車工作量模型。
 */
public class ItemDto {
    private double length;
    private double width;
    private double height;
    private String category;
    private boolean damaged;

    // Getter & Setter
    public double getLength() { return length; }
    public void setLength(double length) { this.length = length; }
    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public boolean isDamaged() { return damaged; }
    public void setDamaged(boolean damaged) { this.damaged = damaged; }
}
