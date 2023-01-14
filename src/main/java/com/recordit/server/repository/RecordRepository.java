package com.recordit.server.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recordit.server.domain.Member;
import com.recordit.server.domain.Record;

public interface RecordRepository extends JpaRepository<Record, Long> {
	// @Query("select r from RECORD r join fetch r.writer join fetch r.recordColor join fetch r.recordIcon"
	// 		+ " where r.id = :id")
	// Optional<Record> findById(Long id);

	Optional<Record> findTopByWriterAndCreatedAtBetweenOrderByCreatedAtDesc(Member writer, LocalDateTime startTime,
			LocalDateTime endTime);
}
