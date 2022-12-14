package com.recordit.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recordit.server.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
