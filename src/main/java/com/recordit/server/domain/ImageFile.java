package com.recordit.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.web.multipart.MultipartFile;

import com.recordit.server.constant.RefType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "IMAGE_FILE")
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = {"REF_TYPE", "REF_ID"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "DELETED_AT is null")
@SQLDelete(sql = "UPDATE IMAGE_FILE SET IMAGE_FILE.DELETED_AT = CURRENT_TIMESTAMP WHERE IMAGE_FILE.IMAGE_FILE_ID = ?")
@Getter
public class ImageFile extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IMAGE_FILE_ID")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "REF_TYPE")
	private RefType refType;

	@Column(name = "REF_ID")
	private Long refId;

	@Column(name = "DOWNLOAD_URL")
	private String downloadUrl;

	@Column(name = "ORIGINAL_NAME")
	private String originalName;

	@Column(name = "SAVE_NAME")
	private String saveName;

	@Column(name = "EXTENSION")
	private String extension;

	@Column(name = "SIZE")
	private Long size;

	private ImageFile(
			RefType refType,
			Long refId,
			String downloadUrl,
			String originalName,
			String saveName,
			String extension,
			Long size
	) {
		this.refType = refType;
		this.refId = refId;
		this.downloadUrl = downloadUrl;
		this.originalName = originalName;
		this.saveName = saveName;
		this.extension = extension;
		this.size = size;
	}

	public static ImageFile of(
			RefType refType,
			Long refId,
			MultipartFile multipartFile,
			String saveName,
			String saveImageUrl
	) {
		return new ImageFile(
				refType,
				refId,
				saveImageUrl,
				multipartFile.getOriginalFilename(),
				saveName,
				multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1),
				multipartFile.getSize()
		);
	}
}
