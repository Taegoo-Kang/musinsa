package com.musinsa.point.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.musinsa.point.api.model.dto.PointSaveParam;
import com.musinsa.point.api.model.type.PointTypes;
import com.musinsa.point.api.service.PointSaveService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(PointSaveController.class)
class PointSaveControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;

    @MockBean
    private PointSaveService pointSaveService;

    @Test
    void test_savePoint() throws Exception {

        final var content = gson.toJson(PointSaveParam.builder()
            .memberId(10003L)
            .pointType(PointTypes.ORDER_CONFIRM.getPointType())
            .serviceAt(13726L)
            .build());

        final var result = mockMvc.perform(MockMvcRequestBuilders.post("/point/save")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
            );

        result.andExpect(status().isOk())
            .andDo(print());
    }
}
