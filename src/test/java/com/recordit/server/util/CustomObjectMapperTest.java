package com.recordit.server.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.recordit.server.dummy.Foo;

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
			Foo foo = new Foo("testField1", "testField2");
			String fooToJsonString = "{\"field1\":\"testField1\",\"field2\":\"testField2\"}";
			Class<Foo> fooClass = Foo.class;

			// when, then
			assertThatCode(() -> CustomObjectMapper.readValue(fooToJsonString, fooClass))
					.doesNotThrowAnyException();
			assertThat(foo).isEqualTo(CustomObjectMapper.readValue(fooToJsonString, fooClass));

		}
	}

}
