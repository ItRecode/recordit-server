package com.recordit.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recordit.server.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByOauthId(String oauthId);

	boolean existsByNickname(String nickname);
}
