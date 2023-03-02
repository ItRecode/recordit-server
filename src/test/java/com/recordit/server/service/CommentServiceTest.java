package com.recordit.server.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.recordit.server.domain.Comment;
import com.recordit.server.domain.Member;
import com.recordit.server.domain.Record;
import com.recordit.server.domain.RecordCategory;
import com.recordit.server.domain.RecordColor;
import com.recordit.server.domain.RecordIcon;
import com.recordit.server.dto.comment.CommentRequestDto;
import com.recordit.server.dto.comment.ModifyCommentRequestDto;
import com.recordit.server.dto.comment.MyCommentRequestDto;
import com.recordit.server.dto.comment.WriteCommentRequestDto;
import com.recordit.server.dto.comment.WriteCommentResponseDto;
import com.recordit.server.exception.comment.CommentNotFoundException;
import com.recordit.server.exception.comment.EmptyContentException;
import com.recordit.server.exception.comment.NotAllowedModifyWhenNonMemberException;
import com.recordit.server.exception.comment.NotMatchCommentWriterException;
import com.recordit.server.exception.member.MemberNotFoundException;
import com.recordit.server.exception.member.NotFoundUserInfoInSessionException;
import com.recordit.server.exception.record.RecordNotFoundException;
import com.recordit.server.repository.CommentRepository;
import com.recordit.server.repository.MemberRepository;
import com.recordit.server.repository.RecordRepository;
import com.recordit.server.util.SessionUtil;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

	@InjectMocks
	private CommentService commentService;

	@Mock
	private ImageFileService imageFileService;

	@Mock
	private CommentRepository commentRepository;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private RecordRepository recordRepository;

	@Mock
	private SessionUtil sessionUtil;

	@Nested
	@DisplayName("댓글을 작성 할 때")
	class 댓글을_작성_할_때 {

		@Test
		@DisplayName("내용과 첨부파일이 모두 비어있다면 예외를 던진다")
		void 내용과_첨부파일이_모두_비어있다면_예외를_던진다() {
			// given
			WriteCommentRequestDto writeCommentRequestDto = WriteCommentRequestDto.builder()
					.recordId(1L)
					.parentId(1L)
					.comment("")
					.build();
			MockMultipartFile multipartFile = mock(MockMultipartFile.class);

			given(imageFileService.isEmptyFile(any(MultipartFile.class)))
					.willReturn(true);

			// when, then
			assertThatThrownBy(() -> commentService.writeComment(writeCommentRequestDto, multipartFile))
					.isInstanceOf(EmptyContentException.class);
		}

		@Test
		@DisplayName("세션에 저장된 사용자가 DB에 존재하지 않으면 예외를 던진다")
		void 세션에_저장된_사용자가_DB에_존재하지_않으면_예외를_던진다() {
			// given
			WriteCommentRequestDto writeCommentRequestDto = WriteCommentRequestDto.builder()
					.recordId(1L)
					.parentId(1L)
					.comment("test")
					.build();
			MockMultipartFile multipartFile = mock(MockMultipartFile.class);

			given(sessionUtil.findUserIdBySession())
					.willReturn(1L);
			given(memberRepository.findById(1L))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> commentService.writeComment(writeCommentRequestDto, multipartFile))
					.isInstanceOf(MemberNotFoundException.class);

		}

		@Test
		@DisplayName("지정한 레코드가 존재하지 않으면 예외를 던진다")
		void 지정한_레코드가_존재하지_않으면_예외를_던진다() {
			// given
			WriteCommentRequestDto writeCommentRequestDto = WriteCommentRequestDto.builder()
					.recordId(1L)
					.parentId(1L)
					.comment("test")
					.build();
			MockMultipartFile multipartFile = mock(MockMultipartFile.class);

			given(sessionUtil.findUserIdBySession())
					.willThrow(new NotFoundUserInfoInSessionException("세션에 사용자 정보가 저장되어 있지 않습니다"));
			given(recordRepository.findById(writeCommentRequestDto.getRecordId()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> commentService.writeComment(writeCommentRequestDto, multipartFile))
					.isInstanceOf(RecordNotFoundException.class);
		}

		@Test
		@DisplayName("지정한 부모 댓글이 존재하지 않으면 예외를 던진다")
		void 지정한_부모_댓글이_존재하지_않으면_예외를_던진다() {
			// given
			WriteCommentRequestDto writeCommentRequestDto = WriteCommentRequestDto.builder()
					.recordId(1L)
					.parentId(1L)
					.comment("test")
					.build();
			MockMultipartFile multipartFile = mock(MockMultipartFile.class);
			Record record = mock(Record.class);

			given(sessionUtil.findUserIdBySession())
					.willThrow(new NotFoundUserInfoInSessionException("세션에 사용자 정보가 저장되어 있지 않습니다"));
			given(recordRepository.findById(writeCommentRequestDto.getRecordId()))
					.willReturn(Optional.of(record));
			given(commentRepository.findById(writeCommentRequestDto.getParentId()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> commentService.writeComment(writeCommentRequestDto, multipartFile))
					.isInstanceOf(CommentNotFoundException.class);
		}

		@Test
		@DisplayName("정상적으로 작성이 완료되면 ID를 반환한다")
		void 정상적으로_작성이_완료되면_ID를_반환한다() {
			// given
			WriteCommentRequestDto writeCommentRequestDto = WriteCommentRequestDto.builder()
					.recordId(1L)
					.parentId(1L)
					.comment("test")
					.build();
			MockMultipartFile multipartFile = mock(MockMultipartFile.class);
			Record record = mock(Record.class);
			Comment parentComment = mock(Comment.class);
			Comment saveComment = mock(Comment.class);

			given(sessionUtil.findUserIdBySession())
					.willThrow(new NotFoundUserInfoInSessionException("세션에 사용자 정보가 저장되어 있지 않습니다"));
			given(recordRepository.findById(writeCommentRequestDto.getRecordId()))
					.willReturn(Optional.of(record));
			given(commentRepository.findById(writeCommentRequestDto.getParentId()))
					.willReturn(Optional.of(parentComment));
			given(commentRepository.save(any()))
					.willReturn(saveComment);
			given(saveComment.getId())
					.willReturn(2L);

			// when
			WriteCommentResponseDto result = commentService.writeComment(
					writeCommentRequestDto,
					multipartFile
			);

			// then
			assertThat(result.getCommentId()).isEqualTo(2L);
			assertThatCode(() -> commentService.writeComment(
					writeCommentRequestDto,
					multipartFile
			)).doesNotThrowAnyException();
		}
	}

	@Nested
	@DisplayName("댓글을 조회시")
	class 댓글을_조회_할_때 {

		private CommentRequestDto commentRequestDto = mock(CommentRequestDto.class);

		@BeforeEach
		void init() {
			given(commentRequestDto.getPage())
					.willReturn(0);
			given(commentRequestDto.getSize())
					.willReturn(1);
		}

		@Nested
		@DisplayName("조회하려는 부모 댓글 ID가 null일 때")
		class 조회하려는_부모_댓글_ID가_null일_때 {

			@BeforeEach
			void init() {
				given(commentRequestDto.getParentId())
						.willReturn(null);
			}

			@Test
			@DisplayName("지정한 레코드 ID가 존재하지 않으면 예외를 던진다")
			void 지정한_레코드_ID가_존재하지_않으면_예외를_던진다() {
				// given
				given(recordRepository.findById(any()))
						.willReturn(Optional.empty());

				// when, then
				assertThatThrownBy(() -> commentService.getCommentsBy(commentRequestDto))
						.isInstanceOf(RecordNotFoundException.class);
			}

		}

		@Nested
		@DisplayName("조회하려는 부모 댓글 ID가 null이 아닐 때")
		class 조회하려는_부모_댓글_ID가_null이_아닐_때 {

			@BeforeEach
			void init() {
				given(commentRequestDto.getParentId())
						.willReturn(1L);
			}

			@Test
			@DisplayName("지정한 부모 댓글이 존재하지 않으면 예외를 던진다")
			void 지정한_부모_댓글이_존재하지_않으면_예외를_던진다() {
				// given
				given(commentRepository.findById(commentRequestDto.getParentId()))
						.willReturn(Optional.empty());

				// when, then
				assertThatThrownBy(() -> commentService.getCommentsBy(commentRequestDto))
						.isInstanceOf(CommentNotFoundException.class);
			}

			@Test
			@DisplayName("부모가 부모를 가질 때 예외를 던진다")
			void 부모가_부모를_가질_때_예외를_던진다() {
				// given
				Comment parentComment = mock(Comment.class);
				Comment grandParentComment = mock(Comment.class);
				given(commentRepository.findById(commentRequestDto.getParentId()))
						.willReturn(Optional.of(parentComment));
				given(parentComment.getParentComment())
						.willReturn(grandParentComment);

				// when, then
				assertThatThrownBy(() -> commentService.getCommentsBy(commentRequestDto))
						.isInstanceOf(IllegalStateException.class);
			}

		}

	}

	@Nested
	@DisplayName("댓글 삭제시")
	class 댓글_삭제시 {
		@Mock
		private Member mockMember;

		@Mock
		private Member otherMockMember;

		@Mock
		private Comment mockComment;

		@Mock
		private Record mockRecord;

		@Test
		@DisplayName("세션에 사용자가 없다면 예외를 던진다")
		void 세션에_사용자가_없다면_예외를_던진다() {
			// given
			given(memberRepository.findById(any()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> commentService.deleteComment(1L, 1L))
					.isInstanceOf(MemberNotFoundException.class);
		}

		@Test
		@DisplayName("삭제할 댓글이 존재하지 않으면 예외를 던진다")
		void 삭제할_댓글이_존재하지_않으면_예외를_던진다() {
			// given
			given(memberRepository.findById(any()))
					.willReturn(Optional.of(mockMember));
			given(commentRepository.findById(any()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> commentService.deleteComment(1L, 1L))
					.isInstanceOf(CommentNotFoundException.class);
		}

		@Test
		@DisplayName("요청 한 사용자와 댓글 작성자가 다를경우 레코드를 못찾는다면 예외를 던진다")
		void 요청_한_사용자와_댓글_작성자가_다를경우_레코드를_못찾는다면_예외를_던진다() {
			// given
			given(memberRepository.findById(any()))
					.willReturn(Optional.of(mockMember));
			given(commentRepository.findById(any()))
					.willReturn(Optional.of(mockComment));
			given(recordRepository.findByIdFetchWriter(any()))
					.willReturn(Optional.empty());

			given(mockComment.getWriter())
					.willReturn(otherMockMember);

			given(otherMockMember.getId())
					.willReturn(1L);
			given(mockMember.getId())
					.willReturn(2L);

			// when, then
			assertThatThrownBy(() -> commentService.deleteComment(1L, 1L))
					.isInstanceOf(RecordNotFoundException.class)
					.hasMessage("레코드 정보를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("요청 한 사용자가 댓글 작성자가 아니면서 레코드 작성자도 아닐 경우 예외를 던진다")
		void 요청_한_사용자가_댓글_작성자가_아니면서_레코드_작성자도_아닐_경우_예외를_던진다() {
			// given
			given(memberRepository.findById(any()))
					.willReturn(Optional.of(mockMember));
			given(commentRepository.findById(any()))
					.willReturn(Optional.of(mockComment));
			given(recordRepository.findByIdFetchWriter(any()))
					.willReturn(Optional.of(mockRecord));

			given(mockComment.getWriter())
					.willReturn(otherMockMember);

			given(mockRecord.getWriter())
					.willReturn(otherMockMember);

			given(otherMockMember.getId())
					.willReturn(1L);
			given(mockMember.getId())
					.willReturn(2L);

			// when, then
			assertThatThrownBy(() -> commentService.deleteComment(1L, 1L))
					.isInstanceOf(NotMatchCommentWriterException.class)
					.hasMessage("로그인한 사용자가 댓글 작성자 또는 레코드 작성자가 아닙니다.");
		}

		@Test
		@DisplayName("댓글 작성자가 삭제 할 경우 예외를 던지지 않는다")
		void 댓글_작성자가_삭제_할_경우_예외를_던지지_않는다() {
			// given
			given(memberRepository.findById(any()))
					.willReturn(Optional.of(mockMember));
			given(commentRepository.findById(any()))
					.willReturn(Optional.of(mockComment));

			given(mockComment.getWriter())
					.willReturn(mockMember);
			given(mockMember.getId())
					.willReturn(1L);

			// when, then
			assertThatCode(() -> commentService.deleteComment(1L, 1L))
					.doesNotThrowAnyException();
		}

		@Test
		@DisplayName("레코드 작성자가 삭제 할 경우 예외를 던지지 않는다")
		void 레코드_작성자가_삭제_할_경우_예외를_던지지_않는다() {
			// given
			given(sessionUtil.findUserIdBySession())
					.willReturn(1L);
			given(memberRepository.findById(any()))
					.willReturn(Optional.of(mockMember));
			given(commentRepository.findById(any()))
					.willReturn(Optional.of(mockComment));
			given(mockComment.getWriter())
					.willReturn(otherMockMember);
			given(mockMember.getId())
					.willReturn(1L);
			given(otherMockMember.getId())
					.willReturn(2L);
			given(recordRepository.findByIdFetchWriter(any()))
					.willReturn(Optional.of(mockRecord));
			given(mockRecord.getWriter())
					.willReturn(mockMember);

			// when, then
			assertThatCode(() -> commentService.deleteComment(1L, 1L))
					.doesNotThrowAnyException();
		}
	}

	@Nested
	@DisplayName("댓글을 수정 할 때")
	class 댓글을_수정_할_때 {
		@Mock
		private Member mockMember;

		@Mock
		private Member otherMockMember;

		@Mock
		private Comment mockComment;

		MockMultipartFile multipartFile = mock(MockMultipartFile.class);

		private final ModifyCommentRequestDto modifyCommentRequestDto = ModifyCommentRequestDto.builder()
				.comment("수정된 댓글")
				.build();

		@Test
		@DisplayName("회원_정보를_찾을 수 없다면 예외를 던진다")
		void 회원_정보를_찾을_수_없다면_예외를_던진다() {
			// given
			given(memberRepository.findById(anyLong()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> commentService.modifyComment(153L, modifyCommentRequestDto, multipartFile))
					.isInstanceOf(MemberNotFoundException.class)
					.hasMessage("회원 정보를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("댓글 정보를 찾을 수 없다면 예외를 던진다")
		void 댓글_정보를_찾을_수_없다면_예외를_던진다() {
			// given
			given(memberRepository.findById(any()))
					.willReturn(Optional.of(mockMember));
			given(commentRepository.findById(any()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> commentService.modifyComment(124L, modifyCommentRequestDto, multipartFile))
					.isInstanceOf(CommentNotFoundException.class)
					.hasMessage("댓글 정보를 가져올 수 없습니다.");
		}

		@Test
		@DisplayName("비회원 댓글이라면 예외를 던진다")
		void 비회원_댓글이라면_예외를_던진다() {
			// given
			given(memberRepository.findById(any()))
					.willReturn(Optional.of(mockMember));
			given(commentRepository.findById(any()))
					.willReturn(Optional.of(mockComment));
			given(mockComment.getWriter())
					.willReturn(null);

			// when, then
			assertThatThrownBy(() -> commentService.modifyComment(1224L, modifyCommentRequestDto, multipartFile))
					.isInstanceOf(NotAllowedModifyWhenNonMemberException.class)
					.hasMessage("비회원 댓글은 수정 불가능합니다.");
		}

		@Test
		@DisplayName("댓글 작성자와 요청한 사용자가 일치하지 않으면 예외를 던진다")
		void 댓글_작성자와_요청한_사용자가_일치하지_않으면_예외를_던진다() {
			// given
			given(memberRepository.findById(any()))
					.willReturn(Optional.of(mockMember));
			given(commentRepository.findById(any()))
					.willReturn(Optional.of(mockComment));
			given(mockComment.getWriter())
					.willReturn(otherMockMember);
			given(mockMember.getId())
					.willReturn(1L);
			given(otherMockMember.getId())
					.willReturn(2L);

			// when, then
			assertThatThrownBy(() -> commentService.modifyComment(1123L, modifyCommentRequestDto, multipartFile))
					.isInstanceOf(NotMatchCommentWriterException.class)
					.hasMessage("로그인한 사용자와 댓글 작성자가 일치하지 않습니다.");
		}

		@Test
		@DisplayName("정상적으로 수정되면 예외를 던지지 않는다")
		void 정상적으로_수정되면_예외를_던지지_않는다() {
			// given
			given(memberRepository.findById(any()))
					.willReturn(Optional.of(mockMember));
			given(commentRepository.findById(any()))
					.willReturn(Optional.of(mockComment));
			given(mockComment.getWriter())
					.willReturn(mockMember);
			given(mockMember.getId())
					.willReturn(1L);

			// when, then
			assertThatCode(() -> commentService.modifyComment(1123L, modifyCommentRequestDto, multipartFile))
					.doesNotThrowAnyException();
		}
	}

	@Nested
	@DisplayName("내가작성한 댓글 조회에서")
	class 내가작성한_댓글_조회에서 {
		private Long memberId = 1L;
		private MyCommentRequestDto myCommentRequestDto = MyCommentRequestDto.builder()
				.page(1)
				.size(10)
				.build();
		Member member = mock(Member.class);
		PageRequest pageRequest = PageRequest.of(
				myCommentRequestDto.getPage(),
				myCommentRequestDto.getSize(),
				Sort.by(Sort.Direction.DESC, "createdAt")
		);

		List<Record> recordList = List.of(mock(Record.class));
		Page<Record> recordPage = new PageImpl<>(recordList, pageRequest, 1);

		@Test
		@DisplayName("회원_정보를_찾을 수 없다면 예외를 던진다")
		void 회원_정보를_찾을_수_없다면_예외를_던진다() {
			// given
			given(sessionUtil.findUserIdBySession())
					.willReturn(memberId);

			given(memberRepository.findById(memberId))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> commentService.getMyComments(myCommentRequestDto))
					.isInstanceOf(MemberNotFoundException.class)
					.hasMessage("회원 정보를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("정상적이라면 예외를 던지지 않는다")
		void 정상적이라면_예외를_던지지_않는다() {
			//given
			given(sessionUtil.findUserIdBySession())
					.willReturn(memberId);

			given(memberRepository.findById(memberId))
					.willReturn(Optional.of(member));

			given(recordRepository.findRecordsByDistinctCommentWriter(member, pageRequest))
					.willReturn(recordPage);

			given(recordPage.getContent().get(0).getRecordCategory())
					.willReturn(mock(RecordCategory.class));

			given(recordPage.getContent().get(0).getRecordIcon())
					.willReturn(mock(RecordIcon.class));

			given(recordPage.getContent().get(0).getRecordColor())
					.willReturn(mock(RecordColor.class));

			//when, then
			assertThatCode(() -> commentService.getMyComments(myCommentRequestDto))
					.doesNotThrowAnyException();
		}
	}
}
