package com.recordit.server.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.recordit.server.domain.RecordCategory;
import com.recordit.server.dto.record.category.RecordCategoryResponseDto;
import com.recordit.server.repository.RecordCategoryRepository;

@ExtendWith({MockitoExtension.class})
class RecordCategoryServiceTest {

	@InjectMocks
	private RecordCategoryService recordCategoryService;

	@Mock
	private RecordCategoryRepository recordCategoryRepository;

	@Test
	@DisplayName("레코드 전제 조회를 테스트한다")
	void 레코드_전제_조회를_테스트한다() {
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

}