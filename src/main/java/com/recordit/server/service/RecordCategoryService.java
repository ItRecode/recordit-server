package com.recordit.server.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recordit.server.domain.RecordCategory;
import com.recordit.server.dto.record.category.RecordCategoryResponseDto;
import com.recordit.server.dto.record.category.SaveRecordCategoryRequestDto;
import com.recordit.server.exception.record.category.HaveParentRecordCategoryException;
import com.recordit.server.exception.record.category.RecordCategoryNotFoundException;
import com.recordit.server.repository.RecordCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecordCategoryService {
	private final RecordCategoryRepository recordCategoryRepository;

	@Transactional(readOnly = true)
	@Cacheable(value = "Categories")
	public List<RecordCategoryResponseDto> getAllRecordCategories() {
		List<RecordCategory> findRecordCategories = recordCategoryRepository.findAllFetchDepthIsOne();

		LinkedHashMap<RecordCategory, List<RecordCategory>> parentToChildren = new LinkedHashMap<>();

		// 부모이면서 자식이 null이 아닌 Map 생성
		parentToChildren.putAll(findRecordCategories.stream()
				.filter(recordCategory -> recordCategory.getParentRecordCategory() != null)
				.collect(Collectors.groupingBy(RecordCategory::getParentRecordCategory)));

		// 부모이면서 자식이 null인 객체 Map에 추가
		findRecordCategories.stream()
				.filter(recordCategory -> recordCategory.getParentRecordCategory() == null)
				.forEach(recordCategory -> parentToChildren.putIfAbsent(recordCategory, Collections.emptyList()));

		List<RecordCategoryResponseDto> result = new ArrayList<>();
		for (RecordCategory parent : parentToChildren.keySet()) {
			// 자식 객체들을 자식 DTO List로 변환
			List<RecordCategoryResponseDto> children = parentToChildren.get(parent)
					.stream()
					.map(
							child -> RecordCategoryResponseDto.builder()
									.recordCategory(child)
									.children(Collections.emptyList())
									.build()
					)
					.collect(Collectors.toList());

			// 자식 DTO 객체들을 부모 DTO 객체에 추가 후 result에 담음
			RecordCategoryResponseDto parentDto = RecordCategoryResponseDto.builder()
					.recordCategory(parent)
					.children(children)
					.build();
			result.add(parentDto);
		}

		return result;
	}

	@CacheEvict(value = "Categories", allEntries = true)
	public void saveRecordCategory(SaveRecordCategoryRequestDto saveRecordCategoryRequestDto) {
		RecordCategory parentRecordCategory = null;

		if (saveRecordCategoryRequestDto.getParentCategoryId() != null) {
			parentRecordCategory = recordCategoryRepository.findById(saveRecordCategoryRequestDto.getParentCategoryId())
					.orElseThrow(() -> new RecordCategoryNotFoundException("지정한 부모 카테고리 정보를 찾을 수 없습니다."));
		}

		if (parentRecordCategory != null && parentRecordCategory.getParentRecordCategory() != null) {
			throw new HaveParentRecordCategoryException("부모 카테고리는 부모 카테고리를 가질 수 없습니다.");
		}

		RecordCategory recordCategory = RecordCategory.of(parentRecordCategory, saveRecordCategoryRequestDto.getName());
		recordCategoryRepository.save(recordCategory);
	}
}
