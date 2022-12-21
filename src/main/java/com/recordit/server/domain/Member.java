package com.recordit.server.domain;

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

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE member SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String password;
	private String nickname;
	private String oauthId;
	@Enumerated(value = EnumType.STRING)
	private LoginType loginType;

	private Member(String password, String nickname, String oauthId, LoginType loginType) {
		this.password = password;
		this.nickname = nickname;
		this.oauthId = oauthId;
		this.loginType = loginType;
	}

	public static Member of(String password, String nickname, String oauthId, LoginType loginType) {
		return new Member(password, nickname, oauthId, loginType);
	}
}
