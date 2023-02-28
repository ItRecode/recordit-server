package com.recordit.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recordit.server.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByOauthId(String oauthId);

	boolean existsByNickname(String nickname);

	@Query(value = "select * from MEMBER "
			+ "where OAUTH_ID = :oauthId "
			+ "and DELETED_AT is not null "
			+ "order by DELETED_AT desc limit 1 "
			, nativeQuery = true)
	Optional<Member> findTopByOauthIdAndDeletedAtIsNotNullOrderByDeletedAtDesc(@Param("oauthId") String oauthId);
}
