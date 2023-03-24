package com.recordit.server.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recordit.server.repository.MemberDeleteHistoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDeleteHistoryService {
	private final MemberDeleteHistoryRepository memberDeleteHistoryRepository;

	@Transactional
	public int deleteOneWeekAgoHistory() {
		return memberDeleteHistoryRepository.deleteByMemberDeletedAtBetween(
				LocalDateTime.of(LocalDate.now().minusWeeks(1L), LocalTime.MIN),
				LocalDateTime.of(LocalDate.now().minusWeeks(1L), LocalTime.MAX)
		);
	}
}
