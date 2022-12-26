package com.recordit.server.service;

import org.springframework.stereotype.Service;

import com.recordit.server.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;

}
