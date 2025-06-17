package net.ubn.td;

import net.ubn.td.entity.PackageTask;
import net.ubn.td.repository.PackageTaskRepository;
import net.ubn.td.service.PackageJobService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
