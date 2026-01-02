package edu.fcu.controller;

import edu.fcu.model.DispatchResultDto;
import edu.fcu.model.ItemDto;
import edu.fcu.model.VehicleAssignment;
import edu.fcu.service.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 派車檢核 API Controller，負責接收前端請求並回傳派車結果。
 */
@RestController
@RequestMapping("/api")
public class LoadController {
    @Autowired
    private Dispatcher dispatcher;

    /**
     * 接收物品資料，進行派車檢核。
     * @param items 前端傳來的物品清單
     * @return 派車分配結果（含成功與失敗）
     */
    @PostMapping("/check-load")
    public ResponseEntity<DispatchResultDto> checkLoad(@RequestBody List<ItemDto> items) {
        if (items == null || items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        // 檢查每個 item 必要欄位
        for (ItemDto item : items) {
            if (item.getLength() <= 0 || item.getWidth() <= 0 || item.getHeight() <= 0 || item.getCategory() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        return ResponseEntity.ok(dispatcher.dispatchWithUnassigned(items));
    }
}
