package com.recordit.server.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.recordit.server.domain.Comment;
import com.recordit.server.domain.Member;
import com.recordit.server.domain.Record;
import com.recordit.server.dto.comment.CommentRequestDto;
import com.recordit.server.dto.comment.WriteCommentRequestDto;
import com.recordit.server.dto.comment.WriteCommentResponseDto;
import com.recordit.server.exception.comment.CommentNotFoundException;
import com.recordit.server.exception.comment.EmptyContentException;
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

		@Test
		@DisplayName("세션에 사용자가 없다면 예외를 던진다")
		void 세션에_사용자가_없다면_예외를_던진다() {
			// given
			given(sessionUtil.findUserIdBySession())
					.willReturn(1L);
			given(memberRepository.findById(any()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> commentService.deleteComment(1L))
					.isInstanceOf(MemberNotFoundException.class);
		}

		@Test
		@DisplayName("삭제할 댓글이 존재하지 않으면 예외를 던진다")
		void 삭제할_댓글이_존재하지_않으면_예외를_던진다() {
			// given
			Member member = mock(Member.class);

			given(sessionUtil.findUserIdBySession())
					.willReturn(1L);
			given(memberRepository.findById(any()))
					.willReturn(Optional.of(member));
			given(commentRepository.findById(any()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> commentService.deleteComment(1L))
					.isInstanceOf(CommentNotFoundException.class);
		}

		@Test
		@DisplayName("댓글 작성자와 요청한 사용자가 일치하지 않으면 예외를 던진다")
		void 댓글_작성자와_요청한_사용자가_일치하지_않으면_예외를_던진다() {
			// given
			Member member = mock(Member.class);
			Member writer = mock(Member.class);
			Comment comment = mock(Comment.class);

			given(sessionUtil.findUserIdBySession())
					.willReturn(1L);
			given(memberRepository.findById(any()))
					.willReturn(Optional.of(member));
			given(commentRepository.findById(any()))
					.willReturn(Optional.of(comment));
			given(comment.getWriter())
					.willReturn(writer);
			given(writer.getId())
					.willReturn(1L);
			given(member.getId())
					.willReturn(2L);

			// when, then
			assertThatThrownBy(() -> commentService.deleteComment(1L))
					.isInstanceOf(NotMatchCommentWriterException.class);
		}

		@Test
		@DisplayName("정상적으로 삭제되면 예외를 던지지 않는다")
		void 정상적으로_삭제되면_예외를_던지지_않는다() {
			// given
			Member member = mock(Member.class);
			Member writer = mock(Member.class);
			Comment comment = mock(Comment.class);

			given(sessionUtil.findUserIdBySession())
					.willReturn(1L);
			given(memberRepository.findById(any()))
					.willReturn(Optional.of(member));
			given(commentRepository.findById(any()))
					.willReturn(Optional.of(comment));
			given(comment.getWriter())
					.willReturn(writer);
			given(writer.getId())
					.willReturn(1L);
			given(member.getId())
					.willReturn(1L);

			// when, then
			assertThatCode(() -> commentService.deleteComment(1L))
					.doesNotThrowAnyException();
		}

	}

}
