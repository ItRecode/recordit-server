package com.recordit.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recordit.server.domain.RecordIcon;

public interface RecordIconRepository extends JpaRepository<RecordIcon, Long> {
	Optional<RecordIcon> findByName(String iconName);
}
