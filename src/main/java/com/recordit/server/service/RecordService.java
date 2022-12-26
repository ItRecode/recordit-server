package com.recordit.server.service;

import org.springframework.stereotype.Service;

import com.recordit.server.repository.RecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecordService {
	private final RecordRepository recordRepository;
}
