package com.recordit.server.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.multipart.MultipartFile;

import com.recordit.server.constant.RefType;
import com.recordit.server.exception.file.EmptyFileException;
import com.recordit.server.exception.file.FileContentTypeNotAllowedException;
import com.recordit.server.repository.ImageFileRepository;
import com.recordit.server.util.S3Uploader;

@ExtendWith(MockitoExtension.class)
class ImageFileServiceTest {

	@InjectMocks
	private ImageFileService imageFileService;

	@Mock
	private ImageFileRepository imageFileRepository;

	@Mock
	private S3Uploader s3Uploader;

	@Mock
	private ApplicationEventPublisher applicationEventPublisher;

	@Nested
	@DisplayName("첨부 파일을 저장할 때")
	class 첨부_파일을_저장할_때 {

		private final RefType refType = Arrays.stream(RefType.values()).findAny().get();
		private final Long refId = 0L;
		private final MultipartFile mock = mock(MultipartFile.class);
		private final List<MultipartFile> mockMultipartFiles = List.of(mock);

		@Test
		@DisplayName("빈 파일이 넘어오면 예외를 던진다")
		void 빈_파일이_넘어오면_예외를_던진다() {
			// given
			given(mock.isEmpty())
					.willReturn(true);

			// when, then
			assertThatThrownBy(() -> imageFileService.saveAttachmentFiles(refType, refId, mockMultipartFiles))
					.isInstanceOf(EmptyFileException.class);
		}

		@Test
		@DisplayName("규정한 ContentType이 아니면 예외를 던진다")
		void 규정한_ContentType이_아니면_예외를_던진다() {
			// given
			given(mock.isEmpty())
					.willReturn(false);
			given(mock.getContentType())
					.willReturn("notImage");

			// when, then
			assertThatThrownBy(() -> imageFileService.saveAttachmentFiles(refType, refId, mockMultipartFiles))
					.isInstanceOf(FileContentTypeNotAllowedException.class);
		}

		@Test
		@DisplayName("정상적으로 파일을 저장할 경우 Url을 응답한다")
		void 정상적으로_파일을_저장할_경우_URL을_응답한다() {
			// given
			given(mock.isEmpty())
					.willReturn(false);
			given(mock.getContentType())
					.willReturn("image");
			given(s3Uploader.upload(any()))
					.willReturn("saveFileName");
			given(s3Uploader.getUrlByFileName(any()))
					.willReturn("saveFileUrl");
			given(mock.getOriginalFilename())
					.willReturn("test.png");

			// when
			List<String> result = imageFileService.saveAttachmentFiles(refType, refId, mockMultipartFiles);

			// then
			assertThat(result.size()).isEqualTo(1);
			assertThat(result.get(0)).isEqualTo("saveFileUrl");
		}

	}
}