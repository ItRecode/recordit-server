package com.recordit.server.service;

import java.util.List;

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
	public List<RecordCategoryResponseDto> getCategories(Long parentRecordCategoryId) {

		RecordCategory parentRecordCategory = null;
		if (parentRecordCategoryId != null) {
			parentRecordCategory = recordCategoryRepository.findById(parentRecordCategoryId)
					.orElseThrow(() -> new RecordCategoryNotFoundException("지정한 카테고리가 존재하지 않습니다."));
			validateParentHasParent(parentRecordCategory);
		}

		List<RecordCategory> findRecordCategories = recordCategoryRepository.findAllByParentRecordCategory(
				parentRecordCategory
		);

		return RecordCategoryResponseDto.of(findRecordCategories);
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

	private void validateParentHasParent(RecordCategory parentRecordCategory) {
		if (parentRecordCategory.getParentRecordCategory() != null) {
			throw new HaveParentRecordCategoryException("부모 카테고리는 부모 카테고리를 가질 수 없습니다.");
		}
	}
}
