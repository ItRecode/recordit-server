package com.recordit.server.service;

import org.springframework.stereotype.Service;

import com.recordit.server.repository.RecordCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecordCategoryService {
	private final RecordCategoryRepository recordCategoryRepository;

}
