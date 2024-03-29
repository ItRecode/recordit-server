package com.recordit.server.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recordit.server.domain.Comment;
import com.recordit.server.domain.Member;
import com.recordit.server.domain.Record;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Long countByRecordAndParentCommentIsNull(Record record);

	@EntityGraph(attributePaths = {"writer"})
	@Query("select c from COMMENT c left join c.writer where c.record = :record and c.parentComment is null")
	Page<Comment> findAllByRecordWithPagination(@Param("record") Record record, Pageable pageable);

	@EntityGraph(attributePaths = "writer")
	@Query("select c from COMMENT c left join c.writer where c.parentComment = :parentComment")
	Page<Comment> findAllByParentComment(@Param("parentComment") Comment parentComment, Pageable pageable);

	Long countAllByParentComment(Comment parentComment);

	List<Comment> findAllByRecordAndParentCommentIsNull(Record record, Pageable pageable);

	Long countByRecordId(Long recordId);

	@EntityGraph(attributePaths = {"record", "record.recordColor", "record.recordIcon"})
	List<Comment> findByRecord(Record fixRecord);

	@EntityGraph(attributePaths = {"record", "record.recordCategory", "record.recordColor", "record.recordIcon"})
	Page<Comment> findByWriter(Member writer, Pageable pageable);

	@Modifying
	@Query("update COMMENT c set c.deletedAt = CURRENT_TIMESTAMP where c.writer = :writer and c.deletedAt is null")
	void deleteByWriter(@Param("writer") Member writer);

	List<Comment> findAllByWriter(Member member);
}
