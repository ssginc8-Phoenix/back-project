package com.ssginc8.docto.file.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssginc8.docto.file.dto.UploadFile;
import com.ssginc8.docto.file.provider.FileProvider;
import com.ssginc8.docto.global.error.exception.fileException.FileUploadFailedException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FIleServiceImpl implements FileService {
	private final AmazonS3 amazonS3;
	private final FileProvider fileProvider;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	// S3에 파일 업로드
	public UploadFile.Response uploadImage(UploadFile.Request request) {
		String folderPath = request.getCategory().getCategoryName() + "/";
		String fileName =
			folderPath + UUID.randomUUID() + "_" + request.getFile().getOriginalFilename(); // 고유한 파일 이름 생성

		// 메타데이터 설정
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(request.getFile().getContentType());
		metadata.setContentLength(request.getFile().getSize());

		try {
			// S3에 파일 업로드 요청 생성
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName,
				request.getFile().getInputStream(),
				metadata);

			// S3에 파일 업로드
			amazonS3.putObject(putObjectRequest);
		} catch (IOException e) {
			throw new FileUploadFailedException();
		}

		getPublicUrl(fileName);

		return UploadFile.Response.builder()
			.category(request.getCategory())
			.fileName(fileName)
			.originalFileName(request.getFile().getOriginalFilename())
			.url(getPublicUrl(fileName))
			.bucket(bucket)
			.fileSize(request.getFile().getSize())
			.fileType(request.getFile().getContentType())
			.build();
	}

	private String getPublicUrl(String fileName) {
		return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, amazonS3.getRegionName(), fileName);
	}
}