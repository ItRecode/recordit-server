package com.recordit.server.controller;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.recordit.server.dto.comment.CommentRequestDto;
import com.recordit.server.dto.comment.CommentResponseDto;
import com.recordit.server.dto.comment.WriteCommentRequestDto;
import com.recordit.server.service.CommentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

	private final CommentService commentService;

	@ApiOperation(
			value = "레코드에 댓글 작성",
			notes = "레코드에 댓글을 작성합니다"
	)
	@ApiResponses({
			@ApiResponse(
					code = 200, message = "API 정상 작동"
			)
	})
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> writeComment(
			@ApiParam(required = true) @RequestPart(required = true) @Valid WriteCommentRequestDto writeCommentRequestDto,
			@ApiParam @RequestPart MultipartFile file
	) {
		return null;
	}

	@ApiOperation(
			value = "레코드의 댓글을 조회",
			notes = "레코드의 댓글을 조회합니다"
	)
	@ApiResponses({
			@ApiResponse(
					code = 200, message = "API 정상 작동 / 댓글 조회 완료",
					response = CommentResponseDto.class
			)
	})
	@GetMapping
	public ResponseEntity<?> getComment(@Valid @RequestBody CommentRequestDto commentRequestDto) {
		return null;
	}
}
