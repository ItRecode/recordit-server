package com.recordit.server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

import com.recordit.server.constant.RefType;
import com.recordit.server.domain.ImageFile;
import com.recordit.server.event.S3ImageRollbackEvent;
import com.recordit.server.exception.file.EmptyFileException;
import com.recordit.server.exception.file.FileContentTypeNotAllowedException;
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
		validateEmptyFile(attachment);
		validateImageContentType(attachment);

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
				.collect(Collectors.toList());
	}

	@TransactionalEventListener(classes = S3ImageRollbackEvent.class, phase = TransactionPhase.AFTER_ROLLBACK)
	public void handleRollback(S3ImageRollbackEvent event) {
		log.warn("S3에 업로드한 이미지 파일 롤백 : {}", event);
		s3Uploader.delete(event.getRollbackFileName());
	}

	@Transactional
	public void deleteAttachmentFiles(List<String> attachmentFileNames) {
		for (String attachmentFileName : attachmentFileNames) {
			s3Uploader.delete(attachmentFileName);
			log.info("저장한 이미지 파일 삭제 : {}", attachmentFileName);
		}
	}

	private void validateEmptyFile(MultipartFile multipartFile) {
		if (multipartFile.isEmpty()) {
			throw new EmptyFileException("요청한 파일이 비어있습니다.");
		}
	}

	private void validateImageContentType(MultipartFile multipartFile) {
		if (!multipartFile.getContentType().startsWith("image")) {
			log.warn("요청 파일 ContentType : {}", multipartFile.getContentType());
			throw new FileContentTypeNotAllowedException("이미지 파일이 아닙니다.");
		}
	}

}
