package net.ubn.td.controller;

import net.ubn.td.repository.FileRecordRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Paths;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "file.upload-dir=uploads-test")
class FileUploadControllerTest {

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
    void uploadFileSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.epub", "application/epub+zip", "hello".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/files/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("M000"))
                .andExpect(jsonPath("$.data.fileId").exists())
                .andExpect(jsonPath("$.data.storedFilename").exists());
    }
}
