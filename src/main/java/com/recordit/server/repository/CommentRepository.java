package com.recordit.server.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recordit.server.domain.Comment;
import com.recordit.server.domain.Record;
import com.recordit.server.dto.record.mix.MixRecordDto;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Long countByRecordAndParentCommentIsNull(Record record);

	@EntityGraph(attributePaths = {"writer"})
	@Query("select c from COMMENT c left join c.writer where c.record = :record and c.parentComment is null")
	Page<Comment> findAllByRecordWithPagination(@Param("record") Record record, Pageable pageable);

	@EntityGraph(attributePaths = "writer")
	@Query("select c from COMMENT c left join c.writer where c.parentComment = :parentComment")
	Page<Comment> findAllByParentComment(@Param("parentComment") Comment parentComment, Pageable pageable);

	Long countAllByParentComment(Comment parentComment);

	List<Comment> findAllByRecord(Record record, Pageable pageable);

	Long countByRecordId(Long recordId);

	@Query("select new com.recordit.server.dto.record.mix.MixRecordDto("
			+ "c.record.id,"
			+ "c.record.recordColor.name,"
			+ "c.record.recordIcon.name,"
			+ "c.id,"
			+ "c.content"
			+ ") "
			+ "from COMMENT c "
			+ "left join c.record "
			+ "left join c.record.recordColor "
			+ "left join c.record.recordIcon "
			+ "where c.record = :fixRecord "
	)
	List<MixRecordDto> findByRecord(@Param("fixRecord") Record fixRecord);
}
