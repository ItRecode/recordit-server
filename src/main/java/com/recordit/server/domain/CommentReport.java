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

@Entity(name = "COMMENT_REPORT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "DELETED_AT is null")
@SQLDelete(sql = "UPDATE COMMENT_REPORT SET COMMENT_REPORT.DELETED_AT = CURRENT_TIMESTAMP WHERE COMMENT_REPORT.COMMENT_REPORT_ID = ?")
@Getter
public class CommentReport extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COMMENT_REPORT_ID")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member reporter;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMMENT_ID")
	private Comment reportedComment;

}
