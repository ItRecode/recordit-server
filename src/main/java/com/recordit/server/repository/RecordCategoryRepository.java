package com.recordit.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recordit.server.domain.RecordCategory;

public interface RecordCategoryRepository extends JpaRepository<RecordCategory, Long> {
}
