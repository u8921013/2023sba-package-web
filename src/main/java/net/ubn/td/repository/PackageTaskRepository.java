package net.ubn.td.repository;

import net.ubn.td.entity.PackageTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

public interface PackageTaskRepository extends JpaRepository<PackageTask, UUID> {
    List<PackageTask> findByStatus(String status);
    List<PackageTask> findByStatusAndModificationDateBefore(String status, LocalDateTime date);
}
