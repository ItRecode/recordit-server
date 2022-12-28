package com.recordit.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recordit.server.domain.ImageFile;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
}
