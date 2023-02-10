package com.recordit.server.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import com.recordit.server.constant.RefType;
import com.recordit.server.domain.Member;
import com.recordit.server.domain.Record;
import com.recordit.server.domain.RecordCategory;
import com.recordit.server.domain.RecordColor;
import com.recordit.server.domain.RecordIcon;
import com.recordit.server.dto.record.ModifyRecordRequestDto;
import com.recordit.server.dto.record.RandomRecordRequestDto;
import com.recordit.server.dto.record.RecentRecordRequestDto;
import com.recordit.server.dto.record.RecentRecordResponseDto;
import com.recordit.server.dto.record.RecordByDateRequestDto;
import com.recordit.server.dto.record.RecordBySearchRequestDto;
import com.recordit.server.dto.record.WriteRecordRequestDto;
import com.recordit.server.dto.record.memory.MemoryRecordRequestDto;
import com.recordit.server.exception.member.MemberNotFoundException;
import com.recordit.server.exception.record.FixRecordNotExistException;
import com.recordit.server.exception.record.NotMatchLoginUserWithRecordWriterException;
import com.recordit.server.exception.record.RecordColorNotFoundException;
import com.recordit.server.exception.record.RecordIconNotFoundException;
import com.recordit.server.exception.record.RecordNotFoundException;
import com.recordit.server.exception.record.category.RecordCategoryNotFoundException;
import com.recordit.server.repository.CommentRepository;
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
	private ImageFileService imageFileService;

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
	private CommentRepository commentRepository;

	@Mock
	private Member mockMember;

	@Mock
	private RecordCategory mockRecordCategory;

	@Mock
	private RecordColor mockRecordColor;

	@Mock
	private RecordIcon mockRecordIcon;

	@Mock
	private Record mockRecord;

	@Nested
	@DisplayName("레코드를 작성 할 때")
	class 레코드를_작성_할_때 {
		private final Long recordCategoryId = 10L;
		private final String title = "오늘 내 생일이야!";
		private final String content = "오늘은 내 20번째 생일입니다. \n모두 축하와 선물을 준비해 주세요.";
		private final String colorName = "icon-purple";
		private final String iconName = "moon";
		private List<MultipartFile> files = List.of();

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
			assertThatThrownBy(() -> recordService.writeRecord(writeRecordRequestDto, files))
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
			assertThatThrownBy(() -> recordService.writeRecord(writeRecordRequestDto, files))
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
			assertThatThrownBy(() -> recordService.writeRecord(writeRecordRequestDto, files))
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
			assertThatThrownBy(() -> recordService.writeRecord(writeRecordRequestDto, files))
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

			given(recordRepository.save(any())).willReturn(mockRecord);
			given(mockRecord.getId()).willReturn(2394L);

			// when, then
			assertThatCode(() -> recordService.writeRecord(writeRecordRequestDto, files))
					.doesNotThrowAnyException();
		}
	}

	@Nested
	@DisplayName("레코드를 단건 조회 할 때")
	class 레코드를_단건_조회_할_때 {
		@Test
		@DisplayName("레코드 정보를 찾을 수 없다면 예외를 던진다")
		void 레코드_정보를_찾을_수_없다면_예외를_던진다() {
			// given
			given(recordRepository.findById(anyLong()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> recordService.getDetailRecord(234L))
					.isInstanceOf(RecordNotFoundException.class)
					.hasMessage("레코드 정보를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("레코드 정보를 찾을 수 있다면 예외를 던지지 않는다")
		void 레코드_정보를_찾을_수_있다면_예외를_던지지_않는다() {
			// given
			Long recordId = 5421L;
			Long recordCategotyId = 5L;

			given(mockRecord.getId()).willReturn(recordId);

			given(mockRecord.getRecordCategory()).willReturn(mockRecordCategory);
			given(mockRecordCategory.getId()).willReturn(recordCategotyId);
			given(mockRecordCategory.getName()).willReturn("축하해주세요");

			given(mockRecord.getWriter()).willReturn(mockMember);
			given(mockMember.getNickname()).willReturn("히니");

			given(mockRecord.getRecordColor()).willReturn(mockRecordColor);
			given(mockRecordColor.getName()).willReturn("icon-pink");

			given(mockRecord.getRecordIcon()).willReturn(mockRecordIcon);
			given(mockRecordIcon.getName()).willReturn("umbrella");

			given(recordRepository.findById(anyLong()))
					.willReturn(Optional.of(mockRecord));

			// when, then
			assertThatCode(() -> recordService.getDetailRecord(recordId))
					.doesNotThrowAnyException();
		}
	}

	@Nested
	@DisplayName("레코드를 삭제 할 때")
	class 레코드를_삭제_할_때 {
		@Mock
		private Member otherMockMember;

		@Test
		@DisplayName("회원_정보를_찾을 수 없다면 예외를 던진다")
		void 회원_정보를_찾을_수_없다면_예외를_던진다() {
			// given
			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> recordService.deleteRecord(231L))
					.isInstanceOf(MemberNotFoundException.class)
					.hasMessage("회원 정보를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("레코드 정보를 찾을 수 없다면 예외를 던진다")
		void 레코드_정보를_찾을_수_없다면_예외를_던진다() {
			// given
			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.of(mockMember));
			given(recordRepository.findByIdFetchWriter(anyLong()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> recordService.deleteRecord(542L))
					.isInstanceOf(RecordNotFoundException.class)
					.hasMessage("레코드 정보를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("로그인 한 사용자와 글 작성자가 다르다면 예외를 던진다")
		void 로그인_한_사용자와_글_작성자가_다르다면_예외를_던진다() {
			// given

			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.of(mockMember));
			given(recordRepository.findByIdFetchWriter(anyLong()))
					.willReturn(Optional.of(mockRecord));
			given(mockRecord.getWriter())
					.willReturn(otherMockMember);
			given(otherMockMember.getId())
					.willReturn(1L);
			given(mockMember.getId())
					.willReturn(23L);

			// when, then
			assertThatThrownBy(() -> recordService.deleteRecord(42L))
					.isInstanceOf(NotMatchLoginUserWithRecordWriterException.class)
					.hasMessage("로그인 한 사용자와 글 작성자가 일치하지 않습니다.");
		}

		@Test
		@DisplayName("정상적이라면 예외를 던지지 않는다")
		void 정상적이라면_예외를_던지지_않는다() {
			// given
			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.of(mockMember));
			given(recordRepository.findByIdFetchWriter(anyLong()))
					.willReturn(Optional.of(mockRecord));
			given(mockRecord.getWriter())
					.willReturn(otherMockMember);
			given(otherMockMember.getId())
					.willReturn(1L);
			given(mockMember.getId())
					.willReturn(1L);

			// when, then
			assertThatCode(() -> recordService.deleteRecord(214L))
					.doesNotThrowAnyException();
		}
	}

	@Nested
	@DisplayName("추억 레코드를 조회할 때")
	class 추억_레코드를_조회할_때 {

		@Test
		@DisplayName("회원_정보를_찾을 수 없다면 예외를 던진다")
		void 회원_정보를_찾을_수_없다면_예외를_던진다() {
			// given
			MemoryRecordRequestDto memoryRecordRequestDto = mock(MemoryRecordRequestDto.class);

			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> recordService.getMemoryRecords(memoryRecordRequestDto))
					.isInstanceOf(MemberNotFoundException.class)
					.hasMessage("회원 정보를 찾을 수 없습니다.");
		}

	}

	@Nested
	@DisplayName("날짜로 레코드를 조회할 때")
	class 날짜로_레코드를_조회할_때 {

		@Test
		@DisplayName("회원_정보를_찾을 수 없다면 예외를 던진다")
		void 회원_정보를_찾을_수_없다면_예외를_던진다() {
			// given
			RecordByDateRequestDto recordByDateRequestDto = mock(RecordByDateRequestDto.class);

			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> recordService.getRecordBy(recordByDateRequestDto))
					.isInstanceOf(MemberNotFoundException.class)
					.hasMessage("회원 정보를 찾을 수 없습니다.");
		}
	}

	@DisplayName("레코드를 수정 할 때")
	class 레코드를_수정_할_때 {
		@Mock
		private Member otherMockMember;

		private final RefType refType = Arrays.stream(RefType.values()).findAny().get();
		private final Long refId = 0L;
		private final MultipartFile mock = mock(MultipartFile.class);
		private final List<MultipartFile> mockMultipartFiles = List.of(mock);

		private final String content = "수정 된 내용";
		private final String colorName = "icon-purple";
		private final String iconName = "moon";
		private List<MultipartFile> files = List.of();
		private List<String> deleteImages = List.of();

		private final ModifyRecordRequestDto modifyRecordRequestDto = ModifyRecordRequestDto.builder()
				.content(content)
				.colorName(colorName)
				.iconName(iconName)
				.deleteImages(deleteImages)
				.build();

		@Test
		@DisplayName("회원_정보를_찾을 수 없다면 예외를 던진다")
		void 회원_정보를_찾을_수_없다면_예외를_던진다() {
			// given
			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> recordService.modifyRecord(12L, modifyRecordRequestDto, files))
					.isInstanceOf(MemberNotFoundException.class)
					.hasMessage("회원 정보를 찾을 수 없습니다.");
		}

		@Test
		void 레코드_정보를_찾을_수_없다면_예외를_던진다() {
			/// given
			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.of(mockMember));
			given(recordRepository.findByIdFetchWriter(anyLong()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> recordService.modifyRecord(12L, modifyRecordRequestDto, files))
					.isInstanceOf(RecordNotFoundException.class)
					.hasMessage("레코드 정보를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("컬러_정보를_찾을 수 없다면 예외를 던진다")
		void 컬러_정보를_찾을_수_없다면_예외를_던진다() {
			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.of(mockMember));
			given(recordRepository.findByIdFetchWriter(anyLong()))
					.willReturn(Optional.of(mockRecord));
			given(recordColorRepository.findByName(anyString()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> recordService.modifyRecord(12L, modifyRecordRequestDto, files))
					.isInstanceOf(RecordColorNotFoundException.class)
					.hasMessage("컬러 정보를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("아이콘 정보를 찾을 수 없다면 예외를 던진다")
		void 아이콘_정보를_찾을_수_없다면_예외를_던진다() {
			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.of(mockMember));
			given(recordRepository.findByIdFetchWriter(anyLong()))
					.willReturn(Optional.of(mockRecord));
			given(recordColorRepository.findByName(anyString()))
					.willReturn(Optional.of(mockRecordColor));
			given(recordIconRepository.findByName(anyString()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> recordService.modifyRecord(12L, modifyRecordRequestDto, files))
					.isInstanceOf(RecordIconNotFoundException.class)
					.hasMessage("아이콘 정보를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("로그인 한 사용자와 글 작성자가 다르다면 예외를 던진다")
		void 로그인_한_사용자와_글_작성자가_다르다면_예외를_던진다() {
			// given
			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.of(mockMember));
			given(recordRepository.findByIdFetchWriter(anyLong()))
					.willReturn(Optional.of(mockRecord));
			given(recordColorRepository.findByName(anyString()))
					.willReturn(Optional.of(mockRecordColor));
			given(recordIconRepository.findByName(anyString()))
					.willReturn(Optional.of(mockRecordIcon));

			given(mockRecord.getWriter())
					.willReturn(otherMockMember);
			given(otherMockMember.getId())
					.willReturn(1L);
			given(mockMember.getId())
					.willReturn(23L);

			// when, then
			assertThatThrownBy(() -> recordService.modifyRecord(12L, modifyRecordRequestDto, files))
					.isInstanceOf(NotMatchLoginUserWithRecordWriterException.class)
					.hasMessage("로그인 한 사용자와 글 작성자가 일치하지 않습니다.");
		}

		@Test
		@DisplayName("정상적이라면 수정이 완료되고 예외를 던지지 않는다")
		void 정상적이라면_수정이_완료되고_예외를_던지지_않는다() {
			// given
			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.of(mockMember));
			given(recordRepository.findByIdFetchWriter(anyLong()))
					.willReturn(Optional.of(mockRecord));
			given(recordColorRepository.findByName(anyString()))
					.willReturn(Optional.of(mockRecordColor));
			given(recordIconRepository.findByName(anyString()))
					.willReturn(Optional.of(mockRecordIcon));

			given(mockRecord.getWriter())
					.willReturn(mockMember);
			given(mockMember.getId())
					.willReturn(1L);

			// when, then
			assertThatCode(() -> recordService.modifyRecord(12L, modifyRecordRequestDto, files))
					.doesNotThrowAnyException();
		}
	}

	@Nested
	@DisplayName("랜덤 레코드를 조회 할 때")
	class 랜덤_레코드를_죠회_할_때 {
		private final RandomRecordRequestDto randomRecordRequestDto = RandomRecordRequestDto
				.builder()
				.recordCategoryId(1L)
				.size(5)
				.build();

		@Test
		@DisplayName("레코드 카테고리를 찾지 못한다면 예외를 던진다")
		void 레코드_카테고리를_찾지_못한다면_예외를_던진다() {
			// when, then
			assertThatThrownBy(() -> recordService.getRandomRecord(randomRecordRequestDto))
					.isInstanceOf(RecordCategoryNotFoundException.class)
					.hasMessage("카테고리 정보를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("정상적이라면 예외를 던지지 않는다")
		void 정상적이라면_예외를_던지지_않는다() {
			// given
			given(recordCategoryRepository.existsById(anyLong()))
					.willReturn(true);
			given(recordRepository.findRandomRecordByRecordCategoryId(any(), anyLong()))
					.willReturn(new ArrayList<>());
			// when, then
			assertThatCode(() -> recordService.getRandomRecord(randomRecordRequestDto))
					.doesNotThrowAnyException();
		}
	}

	@Nested
	@DisplayName("믹스_레코드를_조회_할_때")
	class 믹스_레코드를_조회_할_때 {
		@Test
		@DisplayName("서버에 고정레코드가 존재하지 않을때 예외를 던진다")
		void 서버에_고정레코드가_존재하지_않을때_예외를_던진다() {
			//given
			given(recordRepository.findById(anyLong()))
					.willReturn(Optional.empty());

			//when, then
			assertThatThrownBy(() -> recordService.getMixRecords())
					.isInstanceOf(FixRecordNotExistException.class)
					.hasMessage("서버에 고정 레코드가 존재하지 않습니다.");
		}

		@Test
		@DisplayName("정상적이라면 예외를 던지지 않는다")
		void 정상적이라면_예외를_던지지_않는다() {
			//given
			given(recordRepository.findById(anyLong()))
					.willReturn(Optional.of(mockRecord));

			given(commentRepository.findByRecord(mockRecord))
					.willReturn(new ArrayList<>());

			//when, then
			assertThatCode(() -> recordService.getMixRecords())
					.doesNotThrowAnyException();
		}
	}

	@DisplayName("최신 레코드를 조회_할 때")
	class 최신_레코드를_조회_할_때 {
		RecentRecordRequestDto recentRecordRequestDto = RecentRecordRequestDto.builder()
				.page(0)
				.size(10)
				.build();

		@Test
		@DisplayName("비어있다면 아무것도 조회되지 않는다")
		void 비어있다면_아무것도_조회되지_않는다() {
			//given
			PageRequest pageRequest = PageRequest.of(
					recentRecordRequestDto.getPage(),
					recentRecordRequestDto.getSize(),
					Sort.Direction.DESC,
					"createdAt"
			);

			given(recordRepository.findAllFetchRecordIconAndRecordColor(pageRequest))
					.willReturn(new PageImpl<>(List.of(), pageRequest, 0));
			//when
			Page<RecentRecordResponseDto> recentRecord = recordService.getRecentRecord(
					recentRecordRequestDto);
			//then
			assertEquals(0, recentRecord.getTotalElements());
		}

		@Test
		@DisplayName("비어있지 않다면 정상적으로 조회된다")
		void 비어있지_않다면_정상적으로_조회된다() {
			//given
			List<Record> recordList = List.of(mockRecord);
			PageRequest pageRequest = PageRequest.of(
					recentRecordRequestDto.getPage(),
					recentRecordRequestDto.getSize(),
					Sort.Direction.DESC,
					"createdAt"
			);
			given(mockRecord.getRecordColor())
					.willReturn(mockRecordColor);
			given(mockRecord.getRecordIcon())
					.willReturn(mockRecordIcon);
			given(mockRecord.getId())
					.willReturn(23L);
			given(mockRecord.getTitle())
					.willReturn("레코드 제목입니다");

			given(mockRecordColor.getName())
					.willReturn("color");
			given(mockRecordIcon.getName())
					.willReturn("icon");
			given(recordRepository.findAllFetchRecordIconAndRecordColor(pageRequest))
					.willReturn(new PageImpl<>(recordList, pageRequest, 1));
			//when
			Page<RecentRecordResponseDto> recentRecord = recordService.getRecentRecord(
					recentRecordRequestDto
			);
			//then
			assertEquals(1, recentRecord.getTotalElements());
			assertEquals(23L, recentRecord.getContent().get(0).getRecordId());
			assertEquals("레코드 제목입니다", recentRecord.getContent().get(0).getTitle());
			assertEquals("color", recentRecord.getContent().get(0).getColorName());
			assertEquals("icon", recentRecord.getContent().get(0).getIconName());
		}
	}

	@Nested
	@DisplayName("검색으로 레코드를 조회할 때")
	class 검색으로_레코드를_조회할_때 {
		@Test
		@DisplayName("회원_정보를_찾을 수 없다면 예외를 던진다")
		void 회원_정보를_찾을_수_없다면_예외를_던진다() {
			// given
			RecordBySearchRequestDto recordBySearchRequestDto = mock(RecordBySearchRequestDto.class);

			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> recordService.getRecordsBySearch(recordBySearchRequestDto))
					.isInstanceOf(MemberNotFoundException.class)
					.hasMessage("회원 정보를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("정상적이라면 예외를 던지지 않는다")
		void 정상적이라면_예외를_던지지_않는다() {
			// given
			Long userId = 1L;
			String searchKeyword = "test";
			int page = 0;
			int size = 10;
			Page<Record> records = new PageImpl<>(Collections.emptyList());
			RecordBySearchRequestDto recordBySearchRequestDto = RecordBySearchRequestDto.builder()
					.searchKeyword(searchKeyword)
					.page(page)
					.size(size)
					.build();

			given(sessionUtil.findUserIdBySession())
					.willReturn(userId);

			given(memberRepository.findById(userId)).willReturn(Optional.of(mockMember));

			given(recordRepository.findByWriterAndTitleContaining(
							mockMember,
							searchKeyword,
							PageRequest.of(
									page,
									size,
									Sort.by(Sort.Direction.DESC, "createdAt")
							)
					)
			).willReturn(records);

			// when, then
			assertThatCode(() -> recordService.getRecordsBySearch(recordBySearchRequestDto))
					.doesNotThrowAnyException();
		}
	}
}
