package com.recordit.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recordit.server.domain.Record;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
