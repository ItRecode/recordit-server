package com.recordit.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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

	@Column(name = "NUM_OF_IMAGE")
	private Integer numOfImage;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECORD_COLOR_ID")
	private RecordColor recordColor;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECORD_ICON_ID")
	private RecordIcon recordIcon;

}