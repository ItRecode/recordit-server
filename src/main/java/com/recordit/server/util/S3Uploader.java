package com.recordit.server.util;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.recordit.server.environment.S3Properties;
import com.recordit.server.exception.file.FileInputStreamException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class S3Uploader {

	private final AmazonS3 amazonS3;
	private final S3Properties s3Properties;

	public String upload(@NonNull MultipartFile multipartFile) {
		String fileName = UUID.randomUUID().toString();
		try {
			amazonS3.putObject(
					new PutObjectRequest(
							s3Properties.getBucket(),
							fileName,
							multipartFile.getInputStream(),
							getObjectMetadataBy(multipartFile)
					).withCannedAcl(CannedAccessControlList.PublicRead)
			);
		} catch (IOException e) {
			throw new FileInputStreamException("해당 파일을 읽어올 수 없습니다.");
		}
		return fileName;
	}

	public String getUrlByFileName(String fileName) {
		return amazonS3.getUrl(s3Properties.getBucket(), fileName).toString();
	}

	private ObjectMetadata getObjectMetadataBy(@NonNull MultipartFile multipartFile) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(multipartFile.getSize());
		objectMetadata.setContentType(multipartFile.getContentType());
		return objectMetadata;
	}

}
