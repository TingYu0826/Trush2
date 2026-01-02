package edu.fcu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fcu.model.ItemDto;
import edu.fcu.model.UnassignedItemDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import edu.fcu.service.Dispatcher;
import edu.fcu.model.DispatchResultDto;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoadController.class)
class LoadControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private Dispatcher dispatcher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("正常派車成功（200 OK）")
    void testCheckLoadSuccess() throws Exception {
        ItemDto item = new ItemDto();
        item.setLength(2.0);
        item.setWidth(1.5);
        item.setHeight(0.3);
        item.setCategory("床墊");
        item.setDamaged(false);
        List<ItemDto> items = List.of(item);
        DispatchResultDto mockResult = new DispatchResultDto(List.of(), List.of());
        when(dispatcher.dispatchWithUnassigned(any())).thenReturn(mockResult);
        mockMvc.perform(post("/api/check-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(items)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("空物品清單 → 400 Bad Request")
    void testCheckLoadEmptyList() throws Exception {
        mockMvc.perform(post("/api/check-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[]"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("有 unassignedItems → 回傳失敗資訊")
    void testCheckLoadWithUnassigned() throws Exception {
        ItemDto item = new ItemDto();
        item.setLength(10.0);
        item.setWidth(10.0);
        item.setHeight(1.0);
        item.setCategory("超大");
        item.setDamaged(false);
        List<ItemDto> items = List.of(item);
        UnassignedItemDto unassigned = new UnassignedItemDto(item, "SIZE_EXCEED");
        DispatchResultDto mockResult = new DispatchResultDto(List.of(), List.of(unassigned));
        when(dispatcher.dispatchWithUnassigned(any())).thenReturn(mockResult);
        mockMvc.perform(post("/api/check-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(items)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.unassignedItems").isArray());
    }

    @Test
    @DisplayName("非法輸入（缺欄位 / null）→ 400 Bad Request")
    void testCheckLoadInvalidInput() throws Exception {
        String invalidJson = "[{\"length\":2.0}]";
        mockMvc.perform(post("/api/check-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
