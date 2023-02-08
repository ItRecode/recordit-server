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

	@Nested
	@DisplayName("레코드 카테고리를 조회할 때")
	class 레코드_카테고리를_조회할_때 {

		@Test
		@DisplayName("상위가 아닌 서브 카테고리를 통해 조회하는 경우 예외를 던진다")
		void 상위가_아닌_서브_카테고리를_통해_조회하는_경우_예외를_던진다() {
			// given
			Long parentRecordCategoryId = 3L;
			RecordCategory parentRecordCategory = mock(RecordCategory.class);
			RecordCategory grandParentRecordCategory = mock(RecordCategory.class);

			given(recordCategoryRepository.findById(anyLong()))
					.willReturn(Optional.of(parentRecordCategory));
			given(parentRecordCategory.getParentRecordCategory())
					.willReturn(grandParentRecordCategory);

			// when, then
			assertThatThrownBy(() -> recordCategoryService.getCategories(parentRecordCategoryId))
					.isInstanceOf(HaveParentRecordCategoryException.class);
		}

		@Test
		@DisplayName("정상적으로 조회 될 경우 예외를 던지지 않는다")
		void 정상적으로_조회_될_경우_예외를_던지지_않는다() {
			// given
			Long parentRecordCategoryId = null;
			RecordCategory subRecordCategory1 = mock(RecordCategory.class);
			RecordCategory subRecordCategory2 = mock(RecordCategory.class);

			given(subRecordCategory1.getId())
					.willReturn(1L);
			given(subRecordCategory1.getName())
					.willReturn("하위 카테고리1");
			given(subRecordCategory2.getId())
					.willReturn(2L);
			given(subRecordCategory2.getName())
					.willReturn("하위 카테고리2");
			given(recordCategoryRepository.findAllByParentRecordCategory(null))
					.willReturn(List.of(subRecordCategory1, subRecordCategory2));

			// when
			List<RecordCategoryResponseDto> subCategories = recordCategoryService.getCategories(
					parentRecordCategoryId
			);

			// then
			assertThat(subCategories.size()).isEqualTo(2);
			assertThat(subCategories.get(0).getId()).isEqualTo(1L);
			assertThat(subCategories.get(0).getName()).isEqualTo("하위 카테고리1");
			assertThat(subCategories.get(1).getId()).isEqualTo(2L);
			assertThat(subCategories.get(1).getName()).isEqualTo("하위 카테고리2");
		}
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