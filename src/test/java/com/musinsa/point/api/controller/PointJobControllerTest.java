package com.musinsa.point.api.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(PointJobController.class)
class PointJobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobOperator jobOperator;
    @MockBean
    private JobRegistry jobRegistry;
    @MockBean
    private JobLauncher jobLauncher;

    @Test
    void test_launchPointExpireJob() throws Exception {
        final var result =mockMvc.perform(MockMvcRequestBuilders.post("/point/job/expire"));

        result.andExpect(status().isOk())
            .andDo(print());
    }
}
