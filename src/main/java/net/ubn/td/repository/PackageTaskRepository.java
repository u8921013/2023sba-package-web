package net.ubn.td.repository;

import net.ubn.td.entity.PackageTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PackageTaskRepository extends JpaRepository<PackageTask, UUID> {
}
