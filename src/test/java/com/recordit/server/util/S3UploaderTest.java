package com.recordit.server.util;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.recordit.server.environment.S3Properties;
import com.recordit.server.exception.imagefile.FileInputStreamException;

@ExtendWith(MockitoExtension.class)
class S3UploaderTest {

	@InjectMocks
	private S3Uploader s3Uploader;

	@Mock
	private AmazonS3 amazonS3;

	@Mock
	private S3Properties s3Properties;

	@Mock
	private MockMultipartFile multipartFile;

	private MockedStatic<UUID> mockUUID;

	private final UUID testUUID = UUID.randomUUID();

	@BeforeEach
	void init() {
		mockUUID = mockStatic(UUID.class);
		given(s3Properties.getBucket())
				.willReturn("testBucket");
	}

	@AfterEach
	void afterEach() {
		mockUUID.close();
	}

	@Nested
	@DisplayName("파일을 업로드할 때")
	class 파일을_업로드할_때 {

		@Test
		@DisplayName("MultipartFile을 읽어올 수 없을 경우 예외를 던진다")
		void MultipartFile을_읽어올_수_없을_경우_예외를_던진다() throws Exception {
			// given
			given(UUID.randomUUID())
					.willReturn(testUUID);
			given(multipartFile.getInputStream())
					.willThrow(IOException.class);

			// when, then
			assertThatThrownBy(() -> s3Uploader.upload(multipartFile))
					.isInstanceOf(FileInputStreamException.class);
		}

		@Test
		@DisplayName("정상적으로 읽어올 수 있을 경우 예외를 던지지 않는다")
		void 정상적으로_읽어올_수_있을_경우_예외를_던지지_않는다() {
			// given
			given(UUID.randomUUID())
					.willReturn(testUUID);

			// when
			String fileName = s3Uploader.upload(multipartFile);

			// then
			assertThat(fileName).isEqualTo(testUUID.toString());
		}
	}

}