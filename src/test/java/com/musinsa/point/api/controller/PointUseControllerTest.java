package com.musinsa.point.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.musinsa.point.api.model.dto.PointUseParam;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(PointUseController.class)
class PointUseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointUseController pointUseController;

    private static final Gson gson = new Gson();
    private static String param;

    @BeforeAll
    public static void setParam() {
        param = gson.toJson(PointUseParam.builder()
            .memberId(10003L)
            .orderNo(12345L)
            .useAt(134000L)
            .build());
    }

    @Test
    void test_usePoint() throws Exception {
        final var result = mockMvc.perform(MockMvcRequestBuilders.post("/point/use")
            .content(param)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        result.andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void test_cancelUsedPoint() throws Exception {
        final var result = mockMvc.perform(MockMvcRequestBuilders.post("/point/use/cancel")
            .content(param)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
        );

        result.andExpect(status().isOk())
            .andDo(print());
    }
}
