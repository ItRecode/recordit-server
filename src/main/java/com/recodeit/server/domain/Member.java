package com.recodeit.server.domain;

import com.recodeit.server.constant.LoginType;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
