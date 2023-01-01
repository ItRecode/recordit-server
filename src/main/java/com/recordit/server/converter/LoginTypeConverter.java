package com.recordit.server.converter;

import org.springframework.core.convert.converter.Converter;

import com.recordit.server.constant.LoginType;

public class LoginTypeConverter implements Converter<String, LoginType> {

	@Override
	public LoginType convert(String source) {
		return LoginType.findByString(source);
	}
}
