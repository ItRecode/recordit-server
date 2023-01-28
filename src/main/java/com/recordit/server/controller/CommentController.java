package com.recordit.server.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.recordit.server.dto.comment.CommentRequestDto;
import com.recordit.server.dto.comment.CommentResponseDto;
import com.recordit.server.dto.comment.DeleteCommentRequestDto;
import com.recordit.server.dto.comment.ModifyCommentRequestDto;
import com.recordit.server.dto.comment.WriteCommentRequestDto;
import com.recordit.server.dto.comment.WriteCommentResponseDto;
import com.recordit.server.exception.ErrorMessage;
import com.recordit.server.service.CommentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

	private final CommentService commentService;

	@ApiOperation(
			value = "레코드에 댓글 작성",
			notes = "레코드에 댓글을 작성합니다"
	)
	@ApiResponses({
			@ApiResponse(
					code = 200, message = "API 정상 작동", response = WriteCommentResponseDto.class
			),
			@ApiResponse(
					code = 400, message = "잘못된 요청", response = ErrorMessage.class
			)
	})
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<WriteCommentResponseDto> writeComment(
			@ApiParam(required = true) @RequestPart(required = true) @Valid WriteCommentRequestDto writeCommentRequestDto,
			@ApiParam @RequestPart(required = false) MultipartFile attachment
	) {
		return ResponseEntity.ok(commentService.writeComment(writeCommentRequestDto, attachment));
	}

	@ApiOperation(
			value = "레코드의 댓글을 조회",
			notes = "레코드의 댓글을 조회합니다\t\n\t\n"
					+ "레코드 ID는 필수 지정해야 합니다.\t\n"
					+ "부모 댓글 ID의 경우 지정하지 않으면 레코드의 Depth가 0인 댓글들을 조회하고, "
					+ "ID를 지정하면 해당 부모의 대댓글인 Depth가 1인 댓글들을 조회합니다.\t\n\t\n"
					+ "댓글 조회의 정렬 기준은 댓글 생성 시간으로 오름차순입니다."
	)
	@ApiResponses({
			@ApiResponse(
					code = 200,
					message = "API 정상 작동 / 댓글 조회 완료", response = CommentResponseDto.class
			),
			@ApiResponse(
					code = 400, message = "잘못된 요청",
					response = ErrorMessage.class
			)
	})
	@GetMapping
	public ResponseEntity<CommentResponseDto> getComment(@Valid @ModelAttribute CommentRequestDto commentRequestDto) {
		return ResponseEntity.ok(commentService.getCommentsBy(commentRequestDto));
	}

	@ApiOperation(
			value = "댓글 삭제",
			notes = "댓글을 삭제합니다."
	)
	@ApiResponses({
			@ApiResponse(
					code = 200, message = "댓글 삭제 성공"
			),
			@ApiResponse(
					code = 400,
					message = "로그인이 안되어있거나, 댓글이 없거나, 로그인 한 사용자와 댓글 작성자가 불일치 한 경우",
					response = ErrorMessage.class
			)
	})
	@DeleteMapping("/{commentId}")
	public ResponseEntity deleteComment(
			@PathVariable("commentId") Long commentId,
			@RequestBody DeleteCommentRequestDto deleteCommentRequestDto
	) {
		commentService.deleteComment(commentId, deleteCommentRequestDto);
		return ResponseEntity.ok().build();
	}

	@ApiOperation(
			value = "댓글 수정",
			notes = "댓글을 수정합니다."
	)
	@ApiResponses({
			@ApiResponse(
					code = 200, message = "댓글 수정 성공"
			),
			@ApiResponse(
					code = 400,
					message = "잘못 된 요청",
					response = ErrorMessage.class
			)
	})
	@PutMapping("/{commentId}")
	public ResponseEntity modifyComment(
			@PathVariable("commentId") Long commentId,
			@ApiParam(required = true) @RequestPart(required = true) @Valid ModifyCommentRequestDto modifyCommentRequestDto,
			@ApiParam @RequestPart(required = false) List<MultipartFile> attachments
	) {
		commentService.modifyComment(commentId, modifyCommentRequestDto, attachments);
		return ResponseEntity.ok().build();
	}
}
