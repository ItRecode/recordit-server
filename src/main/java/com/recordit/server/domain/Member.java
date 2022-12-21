package com.recordit.server.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
	@GeneratedValue(generator = "uuid2")
	@Column(name = "MEMBER_ID", columnDefinition = "BINARY(16)")
	private UUID id;
	private String password;
	private String nickname;
	private String oauthId;
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
