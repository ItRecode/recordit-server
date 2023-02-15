package com.recordit.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.recordit.server.dto.record.ModifyRecordRequestDto;
import com.recordit.server.dto.record.WriteRecordRequestDto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "RECORD")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "DELETED_AT is null")
@SQLDelete(sql = "UPDATE RECORD SET RECORD.DELETED_AT = CURRENT_TIMESTAMP WHERE RECORD.RECORD_ID = ?")
@Getter
public class Record extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RECORD_ID")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECORD_CATEGORY_ID")
	private RecordCategory recordCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member writer;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "CONTENT")
	private String content;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECORD_COLOR_ID")
	private RecordColor recordColor;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECORD_ICON_ID")
	private RecordIcon recordIcon;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "record")
	private List<Comment> comments = new ArrayList<>();

	private Record(
			RecordCategory recordCategory,
			Member writer,
			String title,
			String content,
			RecordColor recordColor,
			RecordIcon recordIcon
	) {
		this.recordCategory = recordCategory;
		this.writer = writer;
		this.title = title;
		this.content = content;
		this.recordColor = recordColor;
		this.recordIcon = recordIcon;
	}

	public static Record of(
			WriteRecordRequestDto writeRecordRequestDto,
			RecordCategory recordCategory,
			Member member,
			RecordColor recordColor,
			RecordIcon recordIcon
	) {
		return new Record(
				recordCategory,
				member,
				writeRecordRequestDto.getTitle(),
				writeRecordRequestDto.getContent(),
				recordColor,
				recordIcon
		);
	}

	public Long modify(
			final ModifyRecordRequestDto modifyRecordRequestDto,
			final RecordColor recordColor,
			final RecordIcon recordIcon
	) {
		this.content = modifyRecordRequestDto.getContent();
		this.recordColor = recordColor;
		this.recordIcon = recordIcon;
		return this.id;
	}
}
