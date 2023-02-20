package com.recordit.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recordit.server.domain.RecordCategory;

public interface RecordCategoryRepository extends JpaRepository<RecordCategory, Long> {
	List<RecordCategory> findAllByParentRecordCategory(RecordCategory parentRecordCategory);

	@EntityGraph(attributePaths = {"subcategories"})
	@Query(value = "select rc "
			+ "from RECORD_CATEGORY rc "
			+ "left join rc.subcategories")
	List<RecordCategory> findAllFetchSubCategories();

	@EntityGraph(attributePaths = {"subcategories"})
	@Query(value = "select rc "
			+ "from RECORD_CATEGORY rc "
			+ "left join rc.subcategories "
			+ "where rc.id = :recordCategoryId")
	Optional<RecordCategory> findByIdFetchSubCategories(@Param("recordCategoryId") Long recordCategoryId);
}
