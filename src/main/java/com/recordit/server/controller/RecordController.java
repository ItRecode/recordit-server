package com.recordit.server.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.recordit.server.dto.record.ModifyRecordRequestDto;
import com.recordit.server.dto.record.RandomRecordRequestDto;
import com.recordit.server.dto.record.RandomRecordResponseDto;
import com.recordit.server.dto.record.RecentRecordRequestDto;
import com.recordit.server.dto.record.RecentRecordResponseDto;
import com.recordit.server.dto.record.RecordByDateRequestDto;
import com.recordit.server.dto.record.RecordByDateResponseDto;
import com.recordit.server.dto.record.RecordBySearchRequestDto;
import com.recordit.server.dto.record.RecordBySearchResponseDto;
import com.recordit.server.dto.record.RecordDetailResponseDto;
import com.recordit.server.dto.record.WriteRecordRequestDto;
import com.recordit.server.dto.record.WriteRecordResponseDto;
import com.recordit.server.dto.record.WrittenRecordDayRequestDto;
import com.recordit.server.dto.record.WrittenRecordDayResponseDto;
import com.recordit.server.dto.record.memory.MemoryRecordRequestDto;
import com.recordit.server.dto.record.memory.MemoryRecordResponseDto;
import com.recordit.server.dto.record.mix.MixRecordResponseDto;
import com.recordit.server.exception.ErrorMessage;
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
			@ApiParam @RequestPart(required = false) List<MultipartFile> attachments
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(recordService.writeRecord(writeRecordRequestDto, attachments));
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
			value = "날짜로 작성한 레코드 조회",
			notes = "날짜로 작성한 레코드를 조회합니다."
	)
	@ApiResponses({
			@ApiResponse(
					code = 200, message = "날짜로 작성한 레코드 조회 성공",
					response = RecordByDateResponseDto.class
			),
			@ApiResponse(
					code = 400, message = "잘못된 요청입니다.",
					response = ErrorMessage.class
			)
	})
	@GetMapping
	public ResponseEntity<RecordByDateResponseDto> getTodayWriteRecord(
			@ModelAttribute RecordByDateRequestDto recordByDateRequestDto
	) {
		return ResponseEntity.ok().body(recordService.getRecordBy(recordByDateRequestDto));
	}

	@ApiOperation(
			value = "추억레코드 리스트를 내림차순으로 조회",
			notes = "추억레코드 리스트를 내림차순으로 조회합니다."
	)
	@ApiResponses({
			@ApiResponse(
					code = 200, message = "추억레코드 리스트 조회 성공",
					response = MemoryRecordResponseDto.class
			),
			@ApiResponse(
					code = 400, message = "잘못된 요청",
					response = ErrorMessage.class
			)
	})
	@GetMapping("/memory")
	public ResponseEntity<MemoryRecordResponseDto> getMemoryRecordList(
			@ModelAttribute @Valid MemoryRecordRequestDto memoryRecordRequestDto
	) {
		return ResponseEntity.ok().body(recordService.getMemoryRecords(memoryRecordRequestDto));
	}

	@ApiOperation(
			value = "레코드 삭제",
			notes = "레코드를 삭제합니다."
	)
	@ApiResponses({
			@ApiResponse(
					code = 200, message = "레코드 삭제 성공"
			),
			@ApiResponse(
					code = 400,
					message = "로그인이 안되어있거나, 레코드가 없거나, 로그인 한 사용자와 글 작성자가 불일치 한 경우",
					response = ErrorMessage.class
			)
	})
	@DeleteMapping("/{recordId}")
	public ResponseEntity deleteRecord(
			@PathVariable("recordId") Long recordId
	) {
		recordService.deleteRecord(recordId);
		return ResponseEntity.ok().build();
	}

	@ApiOperation(
			value = "레코드 수정",
			notes = "레코드를 수정합니다."
	)
	@ApiResponses({
			@ApiResponse(
					code = 200, message = "레코드 수정 성공",
					response = Long.class
			),
			@ApiResponse(
					code = 400,
					message = "잘못 된 요청",
					response = ErrorMessage.class
			)
	})
	@PutMapping("/{recordId}")
	public ResponseEntity<Long> modifyRecord(
			@PathVariable("recordId") Long recordId,
			@ApiParam(required = true) @RequestPart(required = true) @Valid ModifyRecordRequestDto modifyRecordRequestDto,
			@ApiParam @RequestPart(required = false) List<MultipartFile> attachments
	) {
		return ResponseEntity.ok().body(recordService.modifyRecord(recordId, modifyRecordRequestDto, attachments));
	}

	@ApiOperation(
			value = "레코드 랜덤 조회",
			notes = "레코드를 랜덤으로 조회합니다."
	)
	@ApiResponses({
			@ApiResponse(
					code = 200, message = "레코드 랜덤 조회 성공",
					response = RandomRecordResponseDto.class
			),
			@ApiResponse(
					code = 400,
					message = "잘못 된 요청",
					response = ErrorMessage.class
			)
	})
	@GetMapping("/random")
	public ResponseEntity<List<RandomRecordResponseDto>> getRandomRecord(
			@ModelAttribute @Valid RandomRecordRequestDto randomRecordRequestDto
	) {
		return ResponseEntity.ok(recordService.getRandomRecord(randomRecordRequestDto));
	}

	@GetMapping("/mix")
	public ResponseEntity<MixRecordResponseDto> getMixRecords() {
		return ResponseEntity.ok().body(recordService.getMixRecords());
	}

	@ApiOperation(
			value = "최신 레코드 조회",
			notes = "최신의 레코드를 조회합니다."
	)
	@ApiResponses({
			@ApiResponse(
					code = 200, message = "최신 레코드 조회 성공"
			)
	})
	@GetMapping("/recent")
	public ResponseEntity<Page<RecentRecordResponseDto>> getRecentRecord(
			@ModelAttribute @Valid RecentRecordRequestDto recentRecordRequestDto
	) {
		return ResponseEntity.ok(recordService.getRecentRecord(recentRecordRequestDto));
	}

	@ApiOperation(
			value = "레코드를 검색으로 조회",
			notes = "레코드를 검색으로 조회합니다."
	)
	@ApiResponses({
			@ApiResponse(
					code = 200, message = "검색으로 레코드 조회 성공",
					response = RandomRecordResponseDto.class
			),
			@ApiResponse(
					code = 400,
					message = "잘못 된 요청",
					response = ErrorMessage.class
			)
	})
	@GetMapping("/search")
	public ResponseEntity<RecordBySearchResponseDto> getRecordsBySearch(
			@ModelAttribute @Valid RecordBySearchRequestDto recordBySearchRequestDto
	) {
		return ResponseEntity.ok().body(recordService.getRecordsBySearch(recordBySearchRequestDto));
	}

	@GetMapping("/days")
	public ResponseEntity<WrittenRecordDayResponseDto> getWrittenRecordDays(
			@Valid WrittenRecordDayRequestDto writtenRecordDayRequestDto
	) {
		return ResponseEntity.ok(recordService.getWrittenRecordDays(writtenRecordDayRequestDto));
	}
}
