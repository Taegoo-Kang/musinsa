package com.musinsa.point.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.musinsa.point.api.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void test_orderLog() throws Exception {
        final var result = mockMvc.perform(MockMvcRequestBuilders.get("/order/log")
            .param("member", "12345")
        );

        result.andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void test_orderConfirm() throws Exception {
        final var result = mockMvc.perform(MockMvcRequestBuilders.post("/order/confirm/10")
            .param("member", "12345")
        );

        result.andExpect(status().isOk())
            .andDo(print());
    }
}
