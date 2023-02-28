package com.recordit.server.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "MEMBER_DELETE_HISTORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberDeleteHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MEMBER_DELETE_HISTORY_ID")
	private Long id;

	@Column(name = "MEMBER_ID")
	private Long memberId;

	@CreationTimestamp
	@Column(name = "MEMBER_DELETED_AT")
	private LocalDateTime memberDeletedAt;

	@Column(name = "MEMBER_DELETE_HISTORY_DELETED_AT")
	private LocalDateTime historyDeletedAt;

	private MemberDeleteHistory(Long memberId) {
		this.memberId = memberId;
	}

	public static MemberDeleteHistory of(Long memberId) {
		return new MemberDeleteHistory(memberId);
	}
}
