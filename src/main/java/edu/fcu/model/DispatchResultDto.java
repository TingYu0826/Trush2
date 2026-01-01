package edu.fcu.model;

import java.util.List;

/**
 * 派車 API 回傳結構，包含成功 assignments 與失敗 unassignedItems。
 */
public class DispatchResultDto {
    private List<VehicleAssignment> assignments;
    private List<UnassignedItemDto> unassignedItems;

    public DispatchResultDto(List<VehicleAssignment> assignments, List<UnassignedItemDto> unassignedItems) {
        this.assignments = assignments;
        this.unassignedItems = unassignedItems;
    }

    public List<VehicleAssignment> getAssignments() { return assignments; }
    public void setAssignments(List<VehicleAssignment> assignments) { this.assignments = assignments; }
    public List<UnassignedItemDto> getUnassignedItems() { return unassignedItems; }
    public void setUnassignedItems(List<UnassignedItemDto> unassignedItems) { this.unassignedItems = unassignedItems; }
}

