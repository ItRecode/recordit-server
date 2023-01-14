package com.recordit.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recordit.server.domain.Comment;
import com.recordit.server.domain.Member;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Long countByWriter(Member writer);
}