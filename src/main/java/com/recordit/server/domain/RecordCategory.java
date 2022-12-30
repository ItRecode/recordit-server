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

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "RECORD_CATEGORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "DELETED_AT is null")
@SQLDelete(sql = "UPDATE RECORD_CATEGORY SET RECORD_CATEGORY.DELETED_AT = CURRENT_TIMESTAMP WHERE RECORD_CATEGORY.RECORD_CATEGORY_ID = ?")
@Getter
public class RecordCategory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RECORD_CATEGORY_ID")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_RECORD_CATEGORY_ID")
	private RecordCategory parentRecordCategory;

	@Column(name = "NAME")
	private String name;

	@OneToMany(mappedBy = "parentRecordCategory")
	private List<RecordCategory> subcategories = new ArrayList<>();

	private RecordCategory(RecordCategory parentRecordCategory, String name) {
		this.parentRecordCategory = parentRecordCategory;
		this.name = name;
	}

	public static RecordCategory of(RecordCategory parentRecordCategory, String name) {
		return new RecordCategory(parentRecordCategory, name);
	}
}
