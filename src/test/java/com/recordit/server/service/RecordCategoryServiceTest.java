package com.recordit.server.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.recordit.server.domain.RecordCategory;
import com.recordit.server.dto.record.category.RecordCategoryResponseDto;
import com.recordit.server.dto.record.category.SaveRecordCategoryRequestDto;
import com.recordit.server.exception.record.category.HaveParentRecordCategoryException;
import com.recordit.server.exception.record.category.RecordCategoryNotFoundException;
import com.recordit.server.repository.RecordCategoryRepository;

@ExtendWith({MockitoExtension.class})
class RecordCategoryServiceTest {

	@InjectMocks
	private RecordCategoryService recordCategoryService;

	@Mock
	private RecordCategoryRepository recordCategoryRepository;

	@Test
	@DisplayName("레코드 카테고리 전체 조회를 테스트한다")
	void 레코드_카테고리_전체_조회를_테스트한다() {
		// given
		RecordCategory recordCategory1 = mock(RecordCategory.class);
		RecordCategory recordCategory2 = mock(RecordCategory.class);
		RecordCategory recordCategory3 = mock(RecordCategory.class);

		given(recordCategory1.getId())
				.willReturn(1L);
		given(recordCategory1.getName())
				.willReturn("recordCategory1");

		given(recordCategory2.getId())
				.willReturn(2L);
		given(recordCategory2.getName())
				.willReturn("recordCategory2");
		given(recordCategory2.getParentRecordCategory())
				.willReturn(recordCategory1);

		given(recordCategory3.getId())
				.willReturn(3L);
		given(recordCategory3.getName())
				.willReturn("recordCategory3");

		given(recordCategoryRepository.findAllFetchDepthIsOne())
				.willReturn(List.of(recordCategory1, recordCategory2, recordCategory3));
		// when
		List<RecordCategoryResponseDto> result = recordCategoryService.getAllRecordCategories();

		// then
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.get(0).getSubcategories().size()).isEqualTo(1);
		assertThat(result.get(0).getSubcategories().get(0).getId()).isEqualTo(2L);
		assertThat(result.get(1).getId()).isEqualTo(3L);
	}

	@Nested
	@DisplayName("레코드 카테고리를 저장_할 때")
	class 레코드_카테고리를_저장_할_때 {
		private final SaveRecordCategoryRequestDto saveRecordCategoryRequestDto = SaveRecordCategoryRequestDto.builder()
				.parentCategoryId(2L)
				.name("슬퍼요")
				.build();

		@Test
		@DisplayName("부모 카테고리를 찾을 수 없는 경우 예외를 던진다")
		void 부모_카테고리를_찾을_수_없는_경우_예외를_던진다() {
			// given
			given(recordCategoryRepository.findById(anyLong()))
					.willReturn(Optional.empty());

			// when, then
			assertThatThrownBy(() -> recordCategoryService.saveRecordCategory(saveRecordCategoryRequestDto))
					.isInstanceOf(RecordCategoryNotFoundException.class)
					.hasMessage("지정한 부모 카테고리 정보를 찾을 수 없습니다.");
		}

		@Test
		@DisplayName("부모 카테고리가 부모 카테고리를 가지는 경우 예외를 던진다")
		void 부모_카테고리가_부모_카테고리를_가지는_경우_예외를_던진다() {
			// given
			RecordCategory parentCategory = mock(RecordCategory.class);
			RecordCategory parentOfParentCategory = mock(RecordCategory.class);

			given(recordCategoryRepository.findById(anyLong()))
					.willReturn(Optional.of(parentCategory));
			given(parentCategory.getParentRecordCategory())
					.willReturn(parentOfParentCategory);

			// when, then
			assertThatThrownBy(() -> recordCategoryService.saveRecordCategory(saveRecordCategoryRequestDto))
					.isInstanceOf(HaveParentRecordCategoryException.class)
					.hasMessage("부모 카테고리는 부모 카테고리를 가질 수 없습니다.");
		}

		@Test
		@DisplayName("정상적이라면 예외를 던지지 않는다")
		void 정상적이라면_예외를_던지지_않는다() {
			// given
			RecordCategory recordCategory1 = mock(RecordCategory.class);
			SaveRecordCategoryRequestDto saveRecordCategoryRequestDto = SaveRecordCategoryRequestDto.builder()
					.parentCategoryId(2L)
					.name("슬퍼요")
					.build();

			given(recordCategoryRepository.findById(anyLong()))
					.willReturn(Optional.of(recordCategory1));

			ArgumentCaptor<RecordCategory> captor = ArgumentCaptor.forClass(RecordCategory.class);

			// when, then
			assertThatCode(() -> recordCategoryService.saveRecordCategory(saveRecordCategoryRequestDto))
					.doesNotThrowAnyException();
			verify(recordCategoryRepository, times(1)).save(captor.capture());
		}
	}
}