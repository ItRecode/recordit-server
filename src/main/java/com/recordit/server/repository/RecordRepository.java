package com.recordit.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recordit.server.domain.Record;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {
    // @Query("select r from RECORD r join fetch r.writer join fetch r.recordColor join fetch r.recordIcon"
    // 		+ " where r.id = :id")
    // Optional<Record> findById(Long id);
    @Query("SELECT Max(r.id) FROM RECORD r")
    Optional<Long> findLatestRecordId();
}
