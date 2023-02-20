package com.recordit.server.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.recordit.server.service.RecordRankingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecordRankingScheduler {

	private final RecordRankingService recordRankingService;

	// 매 30분 마다
	@Scheduled(cron = "0 0/30 * * * ?")
	public void updateRecordRanking() {
		log.info("레코드 랭킹 집계 스케쥴러 실행");
		recordRankingService.updateRecordRanking();
		log.info("레코드 랭킹 집계 성공");
	}
}
