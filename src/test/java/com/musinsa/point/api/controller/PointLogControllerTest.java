package com.musinsa.point.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.musinsa.point.api.service.PointLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@WebMvcTest(PointLogController.class)
class PointLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointLogService pointLogService;

    @Test
    void test_getPointLog() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("searchType", "SAVE");
        params.add("startDate", "2023-03-01");
        params.add("endDate", "2023-03-01");

        final var result = mockMvc.perform(MockMvcRequestBuilders.get("/point/log")
            .params(params)
        );

        result.andExpect(status().isOk())
            .andDo(print());
    }
}
