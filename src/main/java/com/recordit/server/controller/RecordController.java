package com.recordit.server.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.recordit.server.dto.record.MemoryRecordResponseDto;
import com.recordit.server.dto.record.RecordDetailResponseDto;
import com.recordit.server.dto.record.WriteRecordRequestDto;
import com.recordit.server.dto.record.WriteRecordResponseDto;
import com.recordit.server.exception.ErrorMessage;
import com.recordit.server.exception.record.InvalidPageParameterException;
import com.recordit.server.service.RecordService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
public class RecordController {

	private final RecordService recordService;

	@ApiOperation(
			value = "레코드 작성",
			notes = "레코드를 작성합니다."
	)
	@ApiResponses({
			@ApiResponse(
					code = 200, message = "레코드 작성 성공"
			),
			@ApiResponse(
					code = 400, message = "잘못된 요청",
					response = ErrorMessage.class
			)
	})
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<WriteRecordResponseDto> writeRecord(
			@ApiParam(required = true) @RequestPart(required = true) @Valid WriteRecordRequestDto writeRecordRequestDto,
			@ApiParam @RequestPart(required = false) List<MultipartFile> files
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(recordService.writeRecord(writeRecordRequestDto, files));
	}

	@ApiOperation(
			value = "레코드 단건 조회",
			notes = "레코드를 단건 조회합니다."
	)
	@ApiResponses({
			@ApiResponse(
					code = 200, message = "레코드 조회 성공",
					response = RecordDetailResponseDto.class
			),
			@ApiResponse(
					code = 400, message = "레코드가 없는 경우",
					response = ErrorMessage.class
			)
	})
	@GetMapping("/{recordId}")
	public ResponseEntity<RecordDetailResponseDto> getDetailRecord(
			@PathVariable("recordId") Long recordId) {
		return ResponseEntity.ok().body(recordService.getDetailRecord(recordId));
	}

	@ApiOperation(
			value = "추억레코드 리스트를 내림차순으로 7개씩 조회",
			notes = "추억레코드 리스트를 내림차순으로 7개씩 조회합니다."
	)
	@ApiResponses({
			@ApiResponse(
					code = 200, message = "추억레코드 리스트 조회 성공",
					response = RecordDetailResponseDto.class
			),
			@ApiResponse(
					code = 400, message = "페이지 파라미터가 음수일 경우",
					response = ErrorMessage.class
			)
	})
	@GetMapping("memory-list")
	public ResponseEntity<MemoryRecordResponseDto> getMemoryRecordList(
			@RequestParam String pageNum) {
		if (Integer.parseInt(pageNum) < 0) {
			throw new InvalidPageParameterException("페이지 파라미터는 음수일 수 없습니다.");
		}
		return ResponseEntity.ok().body(recordService.getMemoryRecordList(Integer.parseInt(pageNum)));
	}
}
