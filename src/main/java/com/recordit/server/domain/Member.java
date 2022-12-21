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
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE MEMBER SET MEMBER.deletedAt = CURRENT_TIMESTAMP WHERE MEMBER.MEMBER_ID = ?")
@Getter
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MEMBER_ID")
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
