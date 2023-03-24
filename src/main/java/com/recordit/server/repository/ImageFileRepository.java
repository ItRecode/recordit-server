package com.recordit.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.recordit.server.constant.RefType;
import com.recordit.server.domain.ImageFile;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {

	Optional<ImageFile> findByRefTypeAndRefId(RefType refType, Long refId);

	List<ImageFile> findAllByRefTypeAndRefId(RefType refType, Long refId);

	@EntityGraph(attributePaths = "saveName")
	List<ImageFile> findAllByRefTypeAndRefIdIn(RefType refType, List<Long> refId);

	void deleteAllByRefTypeAndRefIdAndSaveNameIn(
			RefType refType,
			Long refId,
			List<String> attachmentFileNames
	);

	void deleteAllByRefTypeAndRefIdIn(RefType refType, List<Long> refIds);
}
