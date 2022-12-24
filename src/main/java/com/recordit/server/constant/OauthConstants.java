package com.recordit.server.constant;

public enum OauthConstants {
	CLIENT_ID("client_id"),
	CLIENT_SECRET("client_secret"),
	CODE("code"),
	GRANT_TYPE("grant_type"),
	REDIRECT_URI("redirect_uri"),
	ID_TOKEN("id_token");

	public final String key;

	OauthConstants(String key) {
		this.key = key;
	}

	public static String getFixGrantType() {
		return "authorization_code";
	}
}
