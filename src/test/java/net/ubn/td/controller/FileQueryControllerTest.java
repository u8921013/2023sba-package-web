package net.ubn.td.controller;

import net.ubn.td.entity.FileRecord;
import net.ubn.td.repository.FileRecordRepository;
import net.ubn.td.util.UuidV7Generator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Paths;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "file.upload-dir=uploads-test")
class FileQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileRecordRepository repository;

    @AfterEach
    void cleanup() throws Exception {
        FileSystemUtils.deleteRecursively(Paths.get("uploads-test"));
        repository.deleteAll();
    }

    @Test
    void lookupFound() throws Exception {
        FileRecord record = new FileRecord();
        record.setId(UuidV7Generator.generate());
        record.setFileId("abc123");
        record.setDir("uploads-test");
        record.setStoredFilename("test.mp3");
        repository.save(record);

        mockMvc.perform(MockMvcRequestBuilders.get("/files/lookup")
                        .param("path", "/uploads-test/test.mp3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fileId").value("abc123"));
    }

    @Test
    void lookupNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/files/lookup")
                        .param("path", "/uploads-test/none.mp3"))
                .andExpect(status().isNotFound());
    }
}
