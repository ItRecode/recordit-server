package com.recordit.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.recordit.server.domain.ImageFile;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
	@Query("SELECT i.downloadUrl FROM IMAGE_FILE i WHERE i.refId = :refId")
	Optional<List<String>> findDownloadUrls(@Param("refId") Long recordId);
}
