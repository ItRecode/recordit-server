package com.recordit.server.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recordit.server.domain.Member;
import com.recordit.server.domain.Record;
import com.recordit.server.domain.RecordCategory;

public interface RecordRepository extends JpaRepository<Record, Long> {

	@EntityGraph(attributePaths = {"writer", "recordColor", "recordIcon"})
	@Query("select r "
			+ "from RECORD r "
			+ "left join r.writer "
			+ "left join r.recordColor "
			+ "left join r.recordIcon "
			+ "where r.writer = :writer and r.createdAt < "
			+ "("
			+ "select coalesce(max(r.createdAt), :dateTime)"
			+ "from RECORD r "
			+ "where r.createdAt >= :dateTime"
			+ ") "
	)
	Page<Record> findByWriterFetchAllCreatedAtBefore(
			@Param("writer") Member writer,
			@Param("dateTime") LocalDateTime dateTime,
			Pageable pageable
	);

	@EntityGraph(attributePaths = {"writer", "recordCategory", "recordIcon", "recordColor"})
	Page<Record> findAllByWriterAndCreatedAtBetweenOrderByCreatedAtDesc(
			Member writer,
			LocalDateTime startTime,
			LocalDateTime endTime,
			Pageable pageable
	);

	@EntityGraph(attributePaths = {"writer", "recordCategory", "recordIcon", "recordColor"})
	Optional<Record> findFirstByWriterAndCreatedAtBetweenOrderByCreatedAtDesc(
			Member writer,
			LocalDateTime startTime,
			LocalDateTime endTime
	);

	@Query("select r from RECORD r join fetch r.writer where r.id = :id")
	Optional<Record> findByIdFetchWriter(Long id);

	@Query(value = "select * from RECORD r "
			+ "where r.DELETED_AT is null "
			+ "and r.RECORD_CATEGORY_ID IN ("
			+ "select c.RECORD_CATEGORY_ID "
			+ "from RECORD_CATEGORY c where c.PARENT_RECORD_CATEGORY_ID = :categoryId"
			+ ") "
			+ "order by RAND() limit :size", nativeQuery = true)
	List<Record> findRandomRecordByRecordCategoryId(Integer size, Long categoryId);

	@EntityGraph(attributePaths = {"recordIcon", "recordColor"})
	@Query(value = "select r from RECORD r "
			+ "where r.deletedAt is null and r.createdAt <= :dateTime")
	Page<Record> findAllByCreatedAtBeforeFetchRecordIconAndRecordColor(Pageable pageable, LocalDateTime dateTime);

	@EntityGraph(attributePaths = {"recordCategory", "recordIcon", "recordColor"})
	Page<Record> findByWriterAndTitleContaining(
			Member writer,
			String searchKeyword,
			Pageable pageable
	);

	@EntityGraph(attributePaths = {"writer", "recordCategory", "comments", "recordIcon", "recordColor"})
	@Query(value = "select r "
			+ "from RECORD r "
			+ "left join r.writer "
			+ "left join r.recordCategory "
			+ "left join r.comments "
			+ "left join r.recordIcon "
			+ "left join r.recordColor"
	)
	List<Record> findAllFetchAll();

	@EntityGraph(attributePaths = {"writer", "recordCategory", "comments", "recordIcon", "recordColor"})
	@Query(value = "select r "
			+ "from RECORD r "
			+ "left join r.writer "
			+ "left join r.recordCategory "
			+ "left join r.comments "
			+ "left join r.recordIcon "
			+ "left join r.recordColor "
			+ "where r.recordCategory in :recordCategories"
	)
	List<Record> findAllInRecordCategoryFetchAll(@Param("recordCategories") List<RecordCategory> recordCategories);

	List<Record> findAllByWriterAndCreatedAtBetween(
			Member writer,
			LocalDateTime startTime,
			LocalDateTime endTime
	);

	@Modifying
	@Query("update from RECORD r "
			+ "set r.deletedAt = CURRENT_TIMESTAMP "
			+ "where r.writer = :writer and r.deletedAt is null")
	void deleteByWriter(@Param("writer") Member writer);

	@Query("select distinct r from RECORD r left join r.comments rs where rs.writer = :writer")
	Page<Record> findDistinctRecordsByCommentWriter(@Param("writer") Member member, Pageable pageable);

	@EntityGraph(attributePaths = {"recordCategory", "recordIcon", "recordColor", "comments"})
	@Query("select r from RECORD r where r in :records order by r.createdAt desc")
	List<Record> findByRecordIn(@Param("records") List<Record> records);

	List<Record> findAllByWriter(Member member);
}
