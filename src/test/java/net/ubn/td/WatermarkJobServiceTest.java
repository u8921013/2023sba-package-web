package net.ubn.td;

import net.ubn.td.entity.FileRecord;
import net.ubn.td.entity.WatermarkTask;
import net.ubn.td.repository.FileRecordRepository;
import net.ubn.td.repository.WatermarkTaskRepository;
import net.ubn.td.service.WatermarkJobService;
import net.ubn.td.service.WatermarkProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WatermarkJobServiceTest {

    @Mock
    WatermarkTaskRepository taskRepository;
    @Mock
    FileRecordRepository fileRepository;
    @Mock
    WatermarkProcessor processor;

    @InjectMocks
    WatermarkJobService service;

    @Test
    void processTaskSuccess() throws Exception {
        Path tempIn = Files.createTempFile("in", ".png");
        Files.writeString(tempIn, "abc");
        Path tempOut = Files.createTempFile("out", ".png");
        Files.writeString(tempOut, "def");

        FileRecord rec = new FileRecord();
        rec.setDir(tempIn.getParent().toString());
        rec.setStoredFilename(tempIn.getFileName().toString());
        rec.setOriginalFilename("test.png");

        WatermarkTask task = new WatermarkTask();
        task.setFileId("fid");
        task.setText("txt");

        when(fileRepository.findByFileId("fid")).thenReturn(Optional.of(rec));
        when(processor.applyWatermark(any(), eq("txt"))).thenReturn(tempOut);

        service.processTask(task);

        assertEquals("done", task.getStatus());
        assertNotNull(task.getOutputFileId());
        assertNull(task.getErrorMessage());
    }

    @Test
    void processTaskError() throws Exception {
        WatermarkTask task = new WatermarkTask();
        task.setFileId("fid");
        task.setText("txt");

        when(fileRepository.findByFileId("fid")).thenThrow(new RuntimeException("boom"));

        service.processTask(task);

        assertEquals("failed", task.getStatus());
        assertEquals("boom", task.getErrorMessage());
    }
}
