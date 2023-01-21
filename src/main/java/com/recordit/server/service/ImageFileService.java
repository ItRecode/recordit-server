package com.recordit.server.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

import com.recordit.server.constant.ImageFileExtension;
import com.recordit.server.constant.RefType;
import com.recordit.server.domain.ImageFile;
import com.recordit.server.event.S3ImageRollbackEvent;
import com.recordit.server.exception.file.FileContentTypeNotAllowedException;
import com.recordit.server.exception.file.FileExtensionNotAllowedException;
import com.recordit.server.repository.ImageFileRepository;
import com.recordit.server.util.S3Uploader;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageFileService {

	private final ImageFileRepository imageFileRepository;
	private final S3Uploader s3Uploader;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Transactional
	public String saveAttachmentFile(
			@NonNull RefType refType,
			@NonNull Long refId,
			@NonNull MultipartFile attachment
	) {
		if (attachment.isEmpty()) {
			return null;
		}
		validateImageContentType(attachment);
		validateFileExtension(attachment);

		String saveFileName = s3Uploader.upload(attachment);
		log.info("S3에 저장한 파일 이름 : {}", saveFileName);
		String saveFileUrl = s3Uploader.getUrlByFileName(saveFileName);
		log.info("S3에 저장한 URL : {}", saveFileUrl);
		applicationEventPublisher.publishEvent(S3ImageRollbackEvent.from(saveFileName));

		imageFileRepository.save(
				ImageFile.of(
						refType,
						refId,
						saveFileUrl,
						saveFileName,
						attachment
				)
		);

		return saveFileUrl;
	}

	@Transactional
	public List<String> saveAttachmentFiles(
			@NonNull RefType refType,
			@NonNull Long refId,
			@NonNull List<MultipartFile> attachments
	) {
		return attachments.stream()
				.map(attachment -> saveAttachmentFile(refType, refId, attachment))
				.filter(saveUrl -> saveUrl != null)
				.collect(Collectors.toList());
	}

	@TransactionalEventListener(classes = S3ImageRollbackEvent.class, phase = TransactionPhase.AFTER_ROLLBACK)
	public void handleRollback(S3ImageRollbackEvent event) {
		log.warn("S3에 업로드한 이미지 파일 롤백 : {}", event);
		s3Uploader.delete(event.getRollbackFileName());
	}

	@Transactional(readOnly = true)
	public String getImageFile(
			@NonNull RefType refType,
			@NonNull Long refId
	) {
		Optional<ImageFile> findImageFile = imageFileRepository.findByRefTypeAndRefId(refType, refId);
		if (findImageFile.isPresent()) {
			return findImageFile.get().getDownloadUrl();
		}
		return null;
	}

	@Transactional(readOnly = true)
	public List<String> getImageFiles(
			@NonNull RefType refType,
			@NonNull Long refId
	) {
		return imageFileRepository.findAllByRefTypeAndRefId(refType, refId).stream()
				.map(ImageFile::getDownloadUrl)
				.collect(Collectors.toList());
	}

	@Transactional
	public void deleteAttachmentFiles(List<String> attachmentFileNames) {
		for (String attachmentFileName : attachmentFileNames) {
			s3Uploader.delete(attachmentFileName);
			log.info("저장한 이미지 파일 삭제 : {}", attachmentFileName);
		}
	}

	private void validateImageContentType(MultipartFile multipartFile) {
		if (!multipartFile.getContentType().startsWith("image")) {
			log.warn("요청 파일 ContentType : {}", multipartFile.getContentType());
			throw new FileContentTypeNotAllowedException("이미지 파일이 아닙니다.");
		}
	}

	private void validateFileExtension(MultipartFile multipartFile) {
		String extension = multipartFile
				.getOriginalFilename()
				.substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
		Arrays.stream(ImageFileExtension.values())
				.filter(imageFileExtension -> imageFileExtension.name().equals(extension))
				.findFirst()
				.orElseThrow(() -> new FileExtensionNotAllowedException("지원하지 않은 파일 확장자입니다."));
	}

	public boolean isEmptyFile(MultipartFile multipartFile) {
		if (multipartFile == null || multipartFile.isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean isEmptyFile(List<MultipartFile> multipartFiles) {
		if (multipartFiles == null) {
			return true;
		}
		for (MultipartFile multipartFile : multipartFiles) {
			if (isEmptyFile(multipartFile)) {
				return true;
			}
		}
		return false;
	}

}
