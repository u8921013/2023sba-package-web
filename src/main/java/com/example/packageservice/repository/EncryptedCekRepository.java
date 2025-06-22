package com.example.packageservice.repository;

import com.example.packageservice.entity.EncryptedCek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EncryptedCekRepository extends JpaRepository<EncryptedCek, UUID> {
}
