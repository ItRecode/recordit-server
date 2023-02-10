package com.recordit.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recordit.server.domain.RecordCategory;

public interface RecordCategoryRepository extends JpaRepository<RecordCategory, Long> {
	List<RecordCategory> findAllByParentRecordCategory(RecordCategory parentRecordCategory);
}
