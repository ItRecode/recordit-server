package com.recordit.server.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import com.recordit.server.domain.Member;
import com.recordit.server.domain.RecordCategory;
import com.recordit.server.domain.RecordColor;
import com.recordit.server.domain.RecordIcon;
import com.recordit.server.dto.record.WriteRecordRequestDto;
import com.recordit.server.exception.member.MemberNotFoundException;
import com.recordit.server.exception.record.RecordColorNotFoundException;
import com.recordit.server.exception.record.RecordIconNotFoundException;
import com.recordit.server.exception.record.category.RecordCategoryNotFoundException;
import com.recordit.server.repository.ImageFileRepository;
import com.recordit.server.repository.MemberRepository;
import com.recordit.server.repository.RecordCategoryRepository;
import com.recordit.server.repository.RecordColorRepository;
import com.recordit.server.repository.RecordIconRepository;
import com.recordit.server.repository.RecordRepository;
import com.recordit.server.util.SessionUtil;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class RecordServiceTest {
	@InjectMocks
	private RecordService recordService;

	@Mock
	private ImageFileRepository imageFileRepository;

	@Mock
	private SessionUtil sessionUtil;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private RecordCategoryRepository recordCategoryRepository;

	@Mock
	private RecordColorRepository recordColorRepository;

	@Mock
	private RecordIconRepository recordIconRepository;

	@Mock
	private RecordRepository recordRepository;

	@Mock
	private Member mockMember;

	@Mock
	private RecordCategory mockRecordCategory;

	@Mock
	private RecordColor mockRecordColor;

	@Mock
	private RecordIcon mockRecordIcon;

	@Nested
	@DisplayName("레코드를 작성 할 때")
	class 레코드를_작성_할_때 {
		private final Long recordCategoryId = 10L;
		private final String title = "오늘 내 생일이야!";
		private final String content = "오늘은 내 20번째 생일입니다. \n모두 축하와 선물을 준비해 주세요.";
		private final String colorName = "icon-purple";
		private final String iconName = "moon";
		private MultipartFile emptyFile = new MockMultipartFile("비어있는 파일", new byte[0]);
		private MultipartFile notEmptyFile = new MockMultipartFile("비어있지 않은 파일", new byte[10]);

		private final WriteRecordRequestDto writeRecordRequestDto = WriteRecordRequestDto.builder()
				.recordCategoryId(recordCategoryId)
				.title(title)
				.content(content)
				.colorName(colorName)
				.iconName(iconName)
				.build();

		@Test
		@DisplayName("회원_정보를_찾을 수 없다면 예외를 던진다")
		void 회원_정보를_찾을_수_없다면_예외를_던진다() {
			// given
			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> recordService.writeRecord(writeRecordRequestDto, emptyFile))
					.isInstanceOf(MemberNotFoundException.class)
					.hasMessage("회원 정보를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("카테고리_정보를_찾을 수 없다면 예외를 던진다")
		void 카테고리_정보를_찾을_수_없다면_예외를_던진다() {
			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.of(mockMember));
			given(recordCategoryRepository.findById(anyLong()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> recordService.writeRecord(writeRecordRequestDto, emptyFile))
					.isInstanceOf(RecordCategoryNotFoundException.class)
					.hasMessage("카테고리 정보를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("컬러_정보를_찾을 수 없다면 예외를 던진다")
		void 컬러_정보를_찾을_수_없다면_예외를_던진다() {
			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.of(mockMember));
			given(recordCategoryRepository.findById(anyLong()))
					.willReturn(Optional.of(mockRecordCategory));
			given(recordColorRepository.findByName(anyString()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> recordService.writeRecord(writeRecordRequestDto, emptyFile))
					.isInstanceOf(RecordColorNotFoundException.class)
					.hasMessage("컬러 정보를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("아이콘 정보를 찾을 수 없다면 예외를 던진다")
		void 아이콘_정보를_찾을_수_없다면_예외를_던진다() {
			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.of(mockMember));
			given(recordCategoryRepository.findById(anyLong()))
					.willReturn(Optional.of(mockRecordCategory));
			given(recordColorRepository.findByName(anyString()))
					.willReturn(Optional.of(mockRecordColor));
			given(recordIconRepository.findByName(anyString()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> recordService.writeRecord(writeRecordRequestDto, emptyFile))
					.isInstanceOf(RecordIconNotFoundException.class)
					.hasMessage("아이콘 정보를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("입력_정보가_올바르다면 예외를 던지지 않는다")
		void 입력_정보가_올바르다면_예외를_던지지_않는다() {
			// given
			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.of(mockMember));
			given(recordCategoryRepository.findById(anyLong()))
					.willReturn(Optional.of(mockRecordCategory));
			given(recordColorRepository.findByName(anyString()))
					.willReturn(Optional.of(mockRecordColor));
			given(recordIconRepository.findByName(anyString()))
					.willReturn(Optional.of(mockRecordIcon));

			// when, then
			assertThatCode(() -> recordService.writeRecord(writeRecordRequestDto, emptyFile))
					.doesNotThrowAnyException();
		}
	}
}