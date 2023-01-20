package com.recordit.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.recordit.server.domain.Record;

public interface RecordRepository extends JpaRepository<Record, Long> {
	// @Query("select r from RECORD r join fetch r.writer join fetch r.recordColor join fetch r.recordIcon"
	// 		+ " where r.id = :id")
	// Optional<Record> findById(Long id);

	@Query("select r from RECORD r join fetch r.writer where r.id = :id")
	Optional<Record> findByIdFetchWriter(Long id);
}
