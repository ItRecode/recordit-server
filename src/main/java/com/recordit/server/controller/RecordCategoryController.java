package com.recordit.server.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.recordit.server.dto.record.category.RecordCategoryResponseDto;
import com.recordit.server.dto.record.category.SaveRecordCategoryRequestDto;
import com.recordit.server.service.RecordCategoryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record/category")
public class RecordCategoryController {

	private final RecordCategoryService recordCategoryService;

	@ApiOperation(
			value = "레코드 카테고리 조회",
			notes = "레코드 카테고리를 조회합니다. \t\n"
					+ "부모 카테고리를 지정하지 않으면 상위 카테고리를 반환하고, \t\n"
					+ "부모 카테고리를 지정하면 해당 부모의 하위 카테고리를 반환합니다."
	)
	@ApiResponses({
			@ApiResponse(
					code = 200, message = "API 정상 작동 / 레코드 카테고리 목록 반환",
					response = RecordCategoryResponseDto.class, responseContainer = "List"
			)
	})
	@GetMapping
	public ResponseEntity<List<RecordCategoryResponseDto>> getAllRecordCategories(
			@ApiParam(value = "조회할 카테고리의 부모 ID") @RequestParam(required = false) Long parentRecordCategoryId
	) {
		return ResponseEntity.ok(recordCategoryService.getCategories(parentRecordCategoryId));
	}

	@PostMapping
	public ResponseEntity saveRecordCategories(
			@RequestBody SaveRecordCategoryRequestDto saveRecordCategoryRequestDto
	) {
		recordCategoryService.saveRecordCategory(saveRecordCategoryRequestDto);
		return new ResponseEntity(HttpStatus.CREATED);
	}
}
