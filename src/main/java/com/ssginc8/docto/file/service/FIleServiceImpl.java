package com.ssginc8.docto.file.service;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssginc8.docto.file.service.dto.UpdateFile;
import com.ssginc8.docto.file.service.dto.UploadFile;
import com.ssginc8.docto.global.error.exception.fileException.FileDeleteFailedException;
import com.ssginc8.docto.global.error.exception.fileException.FileUploadFailedException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FIleServiceImpl implements FileService {
	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	// S3에 파일 업로드
	@Override
	public UploadFile.Result uploadImage(UploadFile.Command command) {
		String folderPath = command.getCategory().getCategoryName() + "/";
		String fileName =
			folderPath + UUID.randomUUID() + "_" + command.getFile().getOriginalFilename(); // 고유한 파일 이름 생성

		// 메타데이터 설정
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(command.getFile().getContentType());
		metadata.setContentLength(command.getFile().getSize());

		try {
			// S3에 파일 업로드 요청 생성
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName,
				command.getFile().getInputStream(),
				metadata);

			// S3에 파일 업로드
			amazonS3.putObject(putObjectRequest);
		} catch (IOException e) {
			throw new FileUploadFailedException();
		}

		getPublicUrl(fileName);

		return UploadFile.Result.builder()
			.category(command.getCategory())
			.fileName(fileName)
			.originalFileName(command.getFile().getOriginalFilename())
			.url(getPublicUrl(fileName))
			.bucket(bucket)
			.fileSize(command.getFile().getSize())
			.fileType(command.getFile().getContentType())
			.build();
	}

	@Override
	public void deleteFile(String fileName) {
		try {
			amazonS3.deleteObject(bucket, fileName);
		} catch (Exception e) {
			throw new FileDeleteFailedException();
		}
	}

	@Override
	public UpdateFile.Result updateFile(UpdateFile.Command command) {
		if (Objects.nonNull(command.getOriginalFileName())) {
			deleteFile(command.getOriginalFileName());
		}

		UploadFile.Result result = uploadImage(UploadFile.Command.builder()
			.file(command.getFile())
			.category(command.getCategory())
			.build());

		return UpdateFile.Result.from(result);
	}

	private String getPublicUrl(String fileName) {
		return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, amazonS3.getRegionName(), fileName);
	}
}