package com.recordit.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recordit.server.domain.RecordColor;

public interface RecordColorRepository extends JpaRepository<RecordColor, Long> {
	Optional<RecordColor> findByName(String colorName);
}
