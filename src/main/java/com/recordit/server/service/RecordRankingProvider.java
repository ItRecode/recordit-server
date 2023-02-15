package com.recordit.server.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.recordit.server.constant.RankingPeriod;
import com.recordit.server.domain.Record;
import com.recordit.server.domain.RecordCategory;
import com.recordit.server.dto.record.ranking.RecordRankingDto;
import com.recordit.server.util.DateTimeUtil;

@Component
public class RecordRankingProvider {

	private static final Integer SIZE_OF_RANKING_RECORD = 10;

	@Cacheable(value = "RecordRanking", key = "{#rankingPeriod, #recordCategory.id}")
	public List<RecordRankingDto> getRecordRanking(
			List<Record> records,
			RankingPeriod rankingPeriod,
			RecordCategory recordCategory
	) {
		List<Record> recordsAfter = getRecordsAfter(records, rankingPeriod);
		List<Record> recordInCategory = getRecordInCategory(recordsAfter, recordCategory);
		List<Record> sortedRecords = sortByNumOfComment(recordInCategory);
		List<Record> topRankRecords = filterTopRankRecord(sortedRecords);
		return topRankRecords.stream().map(RecordRankingDto::of).collect(Collectors.toList());
	}

	@CachePut(value = "RecordRanking", key = "{#rankingPeriod, #recordCategory.id}")
	public List<RecordRankingDto> updateRecordRanking(
			List<Record> records,
			RankingPeriod rankingPeriod,
			RecordCategory recordCategory
	) {
		List<Record> recordsAfter = getRecordsAfter(records, rankingPeriod);
		List<Record> recordInCategory = getRecordInCategory(recordsAfter, recordCategory);
		List<Record> sortedRecords = sortByNumOfComment(recordInCategory);
		List<Record> topRankRecords = filterTopRankRecord(sortedRecords);
		return topRankRecords.stream().map(RecordRankingDto::of).collect(Collectors.toList());
	}

	private List<Record> getRecordsAfter(List<Record> records, RankingPeriod rankingPeriod) {
		LocalDateTime start = DateTimeUtil.getBeforePeriodFromNow(rankingPeriod);
		return records.stream()
				.filter(record -> record.getCreatedAt().isAfter(start))
				.collect(Collectors.toList());
	}

	private List<Record> getRecordInCategory(List<Record> records, RecordCategory recordCategory) {
		return records.stream()
				// 상위 카테고리의 경우 자신의 하위 카테고리를 모두 포함하여 구하도록 함
				.filter(record -> record.getRecordCategory().equals(recordCategory)
						|| recordCategory.getSubcategories().contains(record.getRecordCategory())
				)
				.collect(Collectors.toList());
	}

	private List<Record> sortByNumOfComment(List<Record> records) {
		return records.stream()
				.sorted(Comparator.comparing((Record record) -> record.getComments().size()).reversed())
				.collect(Collectors.toList());
	}

	private List<Record> filterTopRankRecord(List<Record> records) {
		return records.stream()
				.limit(SIZE_OF_RANKING_RECORD)
				.collect(Collectors.toList());
	}
}
