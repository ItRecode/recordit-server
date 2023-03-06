package com.recordit.server.service;

import java.time.LocalDateTime;
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
import com.recordit.server.util.RedisManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordRankingService {

	private final RecordRankingProvider recordRankingProvider;
	private final RecordRepository recordRepository;
	private final RecordCategoryRepository recordCategoryRepository;
	private final RedisManager redisManager;
	private final static String RANKING_AGGREGATION_TIME = "RANKING_AGGREGATION_TIME";

	@Transactional(readOnly = true)
	public RecordRankingResponseDto getRecordRanking(RecordRankingRequestDto recordRankingRequestDto) {
		RecordCategory recordCategory = recordCategoryRepository.findByIdFetchSubCategories(
				recordRankingRequestDto.getRecordCategoryId()
		).orElseThrow(() -> new RecordCategoryNotFoundException("지정한 레코드 카테고리가 존재하지 않습니다."));

		List<RecordCategory> parentCategoryAndSubCategories = recordCategory.getSubcategories();
		parentCategoryAndSubCategories.add(recordCategory);
		List<Record> findRecords = recordRepository.findAllInRecordCategoryFetchAll(parentCategoryAndSubCategories);

		List<RecordRankingDto> recordRanking = recordRankingProvider.getRecordRanking(
				findRecords,
				recordRankingRequestDto.getRankingPeriod(),
				recordCategory
		);

		LocalDateTime rankingAggregationTime = redisManager.get(RANKING_AGGREGATION_TIME, LocalDateTime.class)
				.orElseThrow(() -> new IllegalStateException("랭킹 집계시 집계 시간이 저장되지 않았습니다."));

		return RecordRankingResponseDto.of(recordRanking, rankingAggregationTime);
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
		redisManager.set(RANKING_AGGREGATION_TIME, LocalDateTime.now());
	}

}
