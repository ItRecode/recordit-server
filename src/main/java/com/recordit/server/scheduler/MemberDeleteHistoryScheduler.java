package com.recordit.server.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.recordit.server.service.MemberDeleteHistoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberDeleteHistoryScheduler {
	private final MemberDeleteHistoryService memberDeleteHistoryService;

	@Scheduled(cron = "0 0 0 * * ?")
	public void deleteOneWeekAgoHistory() {
		log.info("일주일이 경과된 회원탈퇴기록 삭제 스케줄러 작동");
		Integer deletedHistoryCount = memberDeleteHistoryService.deleteOneWeekAgoHistory();
		log.info("일주일이 경과된 회원탈퇴기록 삭제 성공");
		log.info("삭제된 회원탈퇴기록 갯수 : {}", deletedHistoryCount);
	}
}
