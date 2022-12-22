package com.recordit.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "RECORD_COLOR")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "DELETED_AT is null")
@SQLDelete(sql = "UPDATE RECORD_COLOR SET RECORD_COLOR.DELETED_AT = CURRENT_TIMESTAMP WHERE RECORD_COLOR.RECORD_COLOR_ID = ?")
@Getter
public class RecordColor extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RECORD_COLOR_ID")
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "HEX_CODE")
	private String hexCode;
}
