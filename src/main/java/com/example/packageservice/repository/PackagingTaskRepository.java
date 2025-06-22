package com.example.packageservice.repository;

import com.example.packageservice.entity.PackagingTask;
import com.example.packageservice.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface PackagingTaskRepository extends JpaRepository<PackagingTask, UUID> {
    List<PackagingTask> findByStatusOrderByCreatedAtAsc(TaskStatus status, org.springframework.data.domain.Pageable pageable);

    List<PackagingTask> findByStatusAndStartedAtBefore(TaskStatus status, Instant cutoff);
}
