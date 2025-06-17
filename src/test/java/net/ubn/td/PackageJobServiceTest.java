package net.ubn.td;

import net.ubn.td.entity.PackageTask;
import net.ubn.td.repository.PackageTaskRepository;
import net.ubn.td.service.PackageJobService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PackageJobServiceTest {

    @Mock
    PackageTaskRepository repository;

    @InjectMocks
    PackageJobService service;

    @Test
    void processTaskSuccess() {
        PackageTask task = new PackageTask();
        task.setPackageType("HLS");
        task.setStatus("initial");

        PackageJobService spy = spy(service);
        doNothing().when(spy).doHlsPackage(any());

        spy.processTask(task);

        assertEquals("done", task.getStatus());
        assertNull(task.getErrorMessage());
    }

    @Test
    void processTaskError() {
        PackageTask task = new PackageTask();
        task.setPackageType("LCP");
        task.setStatus("initial");

        PackageJobService spy = spy(service);
        doThrow(new RuntimeException("boom")).when(spy).doLcpPackage(any());

        spy.processTask(task);

        assertEquals("failed", task.getStatus());
        assertEquals("boom", task.getErrorMessage());
    }

    @Test
    void retryWhenNotExceeded() {
        PackageTask task = new PackageTask();
        task.setPackageType("HLS");
        task.setStatus("processing");
        task.setRetryCount(1);
        task.setModificationDate(LocalDateTime.now().minusHours(2));

        when(repository.findByStatusAndModificationDateBefore(eq("processing"), any())).thenReturn(List.of(task));

        PackageJobService spy = spy(service);
        doNothing().when(spy).processTask(any());

        spy.scanProcessingTasks();

        assertEquals(2, task.getRetryCount());
        verify(spy).processTask(task);
    }

    @Test
    void markFailedWhenRetryExceeded() {
        PackageTask task = new PackageTask();
        task.setPackageType("HLS");
        task.setStatus("processing");
        task.setRetryCount(3);
        task.setModificationDate(LocalDateTime.now().minusHours(2));

        when(repository.findByStatusAndModificationDateBefore(eq("processing"), any())).thenReturn(List.of(task));

        PackageJobService spy = spy(service);
        spy.scanProcessingTasks();

        assertEquals("failed", task.getStatus());
        verify(spy, never()).processTask(any());
        verify(repository).save(task);
    }
}
