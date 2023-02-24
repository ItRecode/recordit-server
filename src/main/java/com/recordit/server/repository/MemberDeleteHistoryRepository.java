package com.recordit.server.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recordit.server.domain.MemberDeleteHistory;

public interface MemberDeleteHistoryRepository extends JpaRepository<MemberDeleteHistory, Long> {
	@Modifying
	@Query("update MEMBER_DELETE_HISTORY m "
			+ "set m.historyDeletedAt = CURRENT_TIMESTAMP "
			+ "where m.memberDeletedAt between :oneWeekAgoStart and :oneWeekAgoEnd "
			+ "and m.historyDeletedAt is null")
	int deleteByMemberDeletedAtBetween(
			@Param("oneWeekAgoStart") LocalDateTime oneWeekAgoStart,
			@Param("oneWeekAgoEnd") LocalDateTime oneWeekAgoEnd
	);
}
