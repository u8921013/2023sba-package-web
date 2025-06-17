package net.ubn.td.repository;

import net.ubn.td.entity.WatermarkTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface WatermarkTaskRepository extends JpaRepository<WatermarkTask, UUID> {
    List<WatermarkTask> findByStatus(String status);
    List<WatermarkTask> findByStatusAndModificationDateBefore(String status, LocalDateTime date);
}
