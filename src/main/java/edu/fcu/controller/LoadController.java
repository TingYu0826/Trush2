package edu.fcu.controller;

import edu.fcu.model.DispatchResultDto;
import edu.fcu.model.ItemDto;
import edu.fcu.model.VehicleAssignment;
import edu.fcu.service.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
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
    public DispatchResultDto checkLoad(@RequestBody List<ItemDto> items) {
        return dispatcher.dispatchWithUnassigned(items);
    }
}
