package com.recordit.server.util;

import static org.assertj.core.api.Assertions.*;

import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CustomObjectMapperTest {

	@Nested
	@DisplayName("입력된 Json 값이")
	class 입력된_Json_값이 {

		@Test
		@DisplayName("잘못된 경우 예외를 던진다")
		void 잘못된_경우_예외를_던진다() {
			// given
			String str = "test";
			Class<Object> anyClassType = Object.class;

			// when, then
			assertThatThrownBy(() -> CustomObjectMapper.readValue(str, anyClassType))
					.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("정상적인 경우 예외를 던지지 않는다")
		void 정상적인_경우_예외를_던지지_않는다() throws Exception {
			// given
			MappingDummy mappingDummy = new MappingDummy("testField1", "testField2");
			String mappingDummyToJsonString = "{\"field1\":\"testField1\",\"field2\":\"testField2\"}";
			Class<MappingDummy> mappingDummyClass = MappingDummy.class;

			// when, then
			assertThatCode(() -> CustomObjectMapper.readValue(mappingDummyToJsonString, mappingDummyClass))
					.doesNotThrowAnyException();
			assertThat(mappingDummy).isEqualTo(
					CustomObjectMapper.readValue(mappingDummyToJsonString, mappingDummyClass));

		}
	}

}

class MappingDummy {
	private String field1;
	private String field2;

	public MappingDummy(String field1, String field2) {
		this.field1 = field1;
		this.field2 = field2;
	}

	public MappingDummy() {
	}

	public String getField1() {
		return field1;
	}

	public String getField2() {
		return field2;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MappingDummy mappingDummy = (MappingDummy)o;
		return Objects.equals(getField1(), mappingDummy.getField1()) && Objects.equals(getField2(),
				mappingDummy.getField2());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getField1(), getField2());
	}
}
