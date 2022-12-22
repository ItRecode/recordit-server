package com.recordit.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recordit.server.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsByNickname(String nickname);
}
