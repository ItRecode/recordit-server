package com.recordit.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.recordit.server.domain.RecordCategory;

public interface RecordCategoryRepository extends JpaRepository<RecordCategory, Long> {

	@Query("select rc from RECORD_CATEGORY rc left join fetch rc.parentRecordCategory")
	List<RecordCategory> findAllFetchDepthIsOne();
}
