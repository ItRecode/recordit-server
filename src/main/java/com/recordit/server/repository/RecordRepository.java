package com.recordit.server.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
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

	@EntityGraph(attributePaths = {"recordCategory", "recordIcon", "recordColor"})
	@Query("select r "
			+ "from RECORD r "
			+ "left join r.recordCategory "
			+ "left join r.recordIcon "
			+ "left join r.recordColor "
			+ "where r.writer = :writer "
			+ "and :startTime <= r.createdAt and r.createdAt <= :endTime "
	)
	Page<Record> findAllByWriterAndCreatedAtBetweenOrderByCreatedAtDesc(
			@Param("writer") Member writer,
			@Param("startTime") LocalDateTime startTime,
			@Param("endTime") LocalDateTime endTime,
			Pageable pageable
	);

	@Query("select r from RECORD r join fetch r.writer where r.id = :id")
	Optional<Record> findByIdFetchWriter(Long id);

	@Query("select count(*) "
			+ "from RECORD r "
			+ "left join r.recordCategory "
			+ "where r.writer = :writer "
			+ "and r.recordCategory.parentRecordCategory = :parentCategory"
	)
	Long countByWriterAndRecordCategory(
			@Param("writer") Member writer,
			@Param("parentCategory") RecordCategory parentCategory
	);

	@Query("select r"
			+ " from RECORD r "
			+ "left join r.recordCategory "
			+ "where r.writer = :writer "
			+ "and r.recordCategory.parentRecordCategory = :parentCategory"
	)
	List<Record> findByWriterAndRecordCategory(
			@Param("writer") Member writer,
			@Param("parentCategory") RecordCategory parentCategory
	);
}
