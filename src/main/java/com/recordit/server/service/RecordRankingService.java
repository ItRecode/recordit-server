package com.recordit.server.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recordit.server.constant.RankingPeriod;
import com.recordit.server.domain.Record;
import com.recordit.server.domain.RecordCategory;
import com.recordit.server.dto.record.ranking.RecordRankingDto;
import com.recordit.server.dto.record.ranking.RecordRankingRequestDto;
import com.recordit.server.dto.record.ranking.RecordRankingResponseDto;
import com.recordit.server.exception.record.category.RecordCategoryNotFoundException;
import com.recordit.server.repository.RecordCategoryRepository;
import com.recordit.server.repository.RecordRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordRankingService {

	private final RecordRankingProvider recordRankingProvider;
	private final RecordRepository recordRepository;
	private final RecordCategoryRepository recordCategoryRepository;

	@Transactional(readOnly = true)
	public RecordRankingResponseDto getRecordRanking(RecordRankingRequestDto recordRankingRequestDto) {
		RecordCategory recordCategory = recordCategoryRepository.findByIdFetchSubCategories(
				recordRankingRequestDto.getRecordCategoryId()
		).orElseThrow(() -> new RecordCategoryNotFoundException("지정한 레코드 카테고리가 존재하지 않습니다."));
		List<Record> findRecords = recordRepository.findAllByRecordCategoryFetchAll(recordCategory);

		List<RecordRankingDto> recordRanking = recordRankingProvider.getRecordRanking(
				findRecords,
				recordRankingRequestDto.getRankingPeriod(),
				recordCategory
		);

		return RecordRankingResponseDto.of(recordRanking);
	}

	@Transactional(readOnly = true)
	public void updateRecordRanking() {
		List<Record> allRecords = recordRepository.findAllFetchAll();
		List<RecordCategory> allRecordCategories = recordCategoryRepository.findAllFetchSubCategories();
		for (RankingPeriod rankingPeriod : RankingPeriod.values()) {
			for (RecordCategory recordCategory : allRecordCategories) {
				recordRankingProvider.updateRecordRanking(allRecords, rankingPeriod, recordCategory);
			}
		}
	}
}
