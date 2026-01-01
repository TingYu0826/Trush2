package edu.fcu.model;

/**
 * 派車失敗物件 DTO，包含物品資料與失敗原因。
 */
public class UnassignedItemDto {
    private ItemDto item;
    private String failReason; // 例如 HEIGHT_EXCEED, SIZE_EXCEED, CATEGORY_NOT_ALLOWED

    public UnassignedItemDto(ItemDto item, String failReason) {
        this.item = item;
        this.failReason = failReason;
    }

    public ItemDto getItem() { return item; }
    public void setItem(ItemDto item) { this.item = item; }
    public String getFailReason() { return failReason; }
    public void setFailReason(String failReason) { this.failReason = failReason; }
}

