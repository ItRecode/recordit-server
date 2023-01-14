package com.recordit.server.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.recordit.server.domain.Member;
import com.recordit.server.domain.Record;

public interface RecordRepository extends JpaRepository<Record, Long> {
	// @Query("select r from RECORD r join fetch r.writer join fetch r.recordColor join fetch r.recordIcon"
	// 		+ " where r.id = :id")
	// Optional<Record> findById(Long id);

	Slice<Record> findByWriterAndCreatedAtBefore(Member writer, LocalDateTime dateTime, Pageable pageable);
}
