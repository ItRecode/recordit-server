package com.recordit.server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recordit.server.dto.record.category.RecordCategoryResponseDto;
import com.recordit.server.repository.RecordCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecordCategoryService {
	private final RecordCategoryRepository recordCategoryRepository;

	@Transactional(readOnly = true)
	public List<RecordCategoryResponseDto> getAllRecordCategories() {
		return recordCategoryRepository.findAll().stream()
				.filter(recordCategory -> recordCategory.getParentRecordCategory() == null)
				.map(
						recordCategory -> RecordCategoryResponseDto.builder()
								.id(recordCategory.getId())
								.name(recordCategory.getName())
								.subcategories(recordCategory.getSubcategories())
								.build()
				).collect(Collectors.toList());
	}
}
