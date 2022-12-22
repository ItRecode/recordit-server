package com.recordit.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.recordit.server.constant.LoginType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "DELETED_AT is null")
@SQLDelete(sql = "UPDATE MEMBER SET MEMBER.DELETED_AT = CURRENT_TIMESTAMP WHERE MEMBER.MEMBER_ID = ?")
@Getter
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MEMBER_ID")
	private Long id;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "NICKNAME")
	private String nickname;

	@Column(name = "OAUTH_ID")
	private String oauthId;

	@Column(name = "LOGIN_TYPE")
	@Enumerated(value = EnumType.STRING)
	private LoginType loginType;

	private Member(String username, String password, String nickname, String oauthId, LoginType loginType) {
		this.password = password;
		this.nickname = nickname;
		this.oauthId = oauthId;
		this.loginType = loginType;
	}

	public static Member of(String username, String password, String nickname, String oauthId, LoginType loginType) {
		return new Member(username, password, nickname, oauthId, loginType);
	}
}
