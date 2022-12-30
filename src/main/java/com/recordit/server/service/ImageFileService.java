package com.recordit.server.service;

import java.util.ArrayList;
import java.util.List;

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

@Service
@RequiredArgsConstructor
public class ImageFileService {

	private final ImageFileRepository imageFileRepository;
	private final S3Uploader s3Uploader;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Transactional
	public List<String> saveAttachmentFiles(
			@NonNull RefType refType,
			@NonNull Long refId,
			@NonNull List<MultipartFile> attachments
	) {
		List<String> imageUrls = new ArrayList<>();
		for (MultipartFile multipartFile : attachments) {
			validateEmptyFile(multipartFile);
			validateImageContentType(multipartFile);

			String saveFileName = s3Uploader.upload(multipartFile);
			String saveFileUrl = s3Uploader.getUrlByFileName(saveFileName);
			applicationEventPublisher.publishEvent(S3ImageRollbackEvent.from(saveFileName));

			imageFileRepository.save(
					ImageFile.of(
							refType,
							refId,
							saveFileUrl,
							saveFileName,
							multipartFile
					)
			);
			imageUrls.add(saveFileUrl);
		}

		return imageUrls;
	}

	@TransactionalEventListener(classes = S3ImageRollbackEvent.class, phase = TransactionPhase.AFTER_ROLLBACK)
	public void handleRollback(S3ImageRollbackEvent event) {
		s3Uploader.delete(event.getRollbackFileName());
	}

	@Transactional
	public void deleteAttachmentFiles(List<String> attachmentFileNames) {
		for (String attachmentFileName : attachmentFileNames) {
			s3Uploader.delete(attachmentFileName);
		}
	}

	private void validateEmptyFile(MultipartFile multipartFile) {
		if (multipartFile.isEmpty()) {
			throw new EmptyFileException("요청한 파일이 비어있습니다.");
		}
	}

	private void validateImageContentType(MultipartFile multipartFile) {
		if (!multipartFile.getContentType().startsWith("image")) {
			throw new FileContentTypeNotAllowedException("이미지 파일이 아닙니다.");
		}
	}

}
