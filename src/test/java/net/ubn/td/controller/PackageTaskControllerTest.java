package net.ubn.td.controller;

import net.ubn.td.repository.PackageTaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PackageTaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PackageTaskRepository repository;

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @Test
    void createTaskSuccess() throws Exception {
        String json = "{\"fileId\":\"abc\",\"packageType\":\"lcp\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/package/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("M000"))
                .andExpect(jsonPath("$.data.taskId").exists());
    }
}
