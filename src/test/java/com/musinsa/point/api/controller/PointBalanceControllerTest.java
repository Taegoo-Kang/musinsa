package com.musinsa.point.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.musinsa.point.api.service.PointBalanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(PointBalanceController.class)
class PointBalanceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;

    @MockBean
    private PointBalanceService pointBalanceService;

    @Test
    void test_getPointBalance() throws Exception {

        final var result = mockMvc.perform(MockMvcRequestBuilders.get("/point/balance")
            .param("member", "10003")
        );

        result.andExpect(status().isOk())
            .andDo(print());
    }
}
