package net.ubn.td.repository;

import net.ubn.td.entity.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileRecordRepository extends JpaRepository<FileRecord, UUID> {
}
