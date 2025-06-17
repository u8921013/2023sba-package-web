package net.ubn.td.controller;

import net.ubn.td.repository.WatermarkTaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "file.upload-dir=uploads-test")
class WatermarkTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WatermarkTaskRepository repository;

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @Test
    void createTaskSuccess() throws Exception {
        String json = "{\"fileId\":\"abc\",\"text\":\"hello\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/watermark/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("M000"))
                .andExpect(jsonPath("$.data.taskId").exists());
    }
}
