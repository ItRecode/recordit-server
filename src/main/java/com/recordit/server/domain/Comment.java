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

import com.recordit.server.dto.comment.ModifyCommentRequestDto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "COMMENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "DELETED_AT is null")
@SQLDelete(sql = "UPDATE COMMENT SET COMMENT.DELETED_AT = CURRENT_TIMESTAMP WHERE COMMENT.COMMENT_ID = ?")
@Getter
public class Comment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COMMENT_ID")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member writer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECORD_ID")
	private Record record;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_COMMENT_ID")
	private Comment parentComment;

	@Column(name = "CONTENT")
	private String content;

	private Comment(Member writer, Record record, Comment parentComment, String content) {
		this.writer = writer;
		this.record = record;
		this.parentComment = parentComment;
		this.content = content;
	}

	public static Comment of(Member writer, Record record, Comment parentComment, String content) {
		return new Comment(writer, record, parentComment, content);
	}

	public void modify(
			final ModifyCommentRequestDto modifyCommentRequestDto
	) {
		this.content = modifyCommentRequestDto.getComment();
	}
}
