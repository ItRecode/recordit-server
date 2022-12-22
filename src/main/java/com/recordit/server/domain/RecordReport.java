package com.recordit.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "RECORD_REPORT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "DELETED_AT is null")
@SQLDelete(sql = "UPDATE RECORD_REPORT SET RECORD_REPORT.DELETED_AT = CURRENT_TIMESTAMP WHERE RECORD_REPORT.RECORD_REPORT_ID = ?")
@Getter
public class RecordReport extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RECORD_REPORT_ID")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member reporter;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECORD_ID")
	private Record reportedRecord;

}
