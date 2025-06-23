// src/main/java/com/ssginc8/docto/insurance/service/InsuranceDocumentServiceImpl.java
package com.ssginc8.docto.insurance.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import com.ssginc8.docto.file.entity.Category;
import com.ssginc8.docto.file.entity.File;
import com.ssginc8.docto.file.provider.FileProvider;
import com.ssginc8.docto.global.error.exception.fileException.FileUploadFailedException;
import com.ssginc8.docto.insurance.entity.InsuranceDocument;
import com.ssginc8.docto.insurance.provider.InsuranceDocumentProvider;
import com.ssginc8.docto.insurance.repo.InsuranceDocumentRepo;
import com.ssginc8.docto.insurance.service.dto.DocumentApproval;
import com.ssginc8.docto.insurance.service.dto.DocumentResponse;
import com.ssginc8.docto.insurance.service.dto.FileDownload;
import com.ssginc8.docto.insurance.service.dto.UserDocumentRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InsuranceDocumentServiceImpl implements InsuranceDocumentService {

	private final InsuranceDocumentRepo repo;
	private final InsuranceDocumentProvider provider;
	private final AmazonS3 amazonS3;
	private final FileProvider fileProvider;

	// S3 버킷 이름은 application.yml 에서 주입받도록 개선하세요
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	/**
	 * 1) 환자·보호자용: 첨부 없이 요청만 생성
	 */
	@Override
	@Transactional
	public DocumentResponse createRequest(UserDocumentRequest dto) {
		// ● 엔티티 생성 (status=REQUESTED, file=null)
		InsuranceDocument doc = InsuranceDocument.createRequest(
			dto.getRequesterId(), dto.getNote()
		);
		// ● DB에 저장
		repo.save(doc);
		// ● 간단 DTO로 응답
		return DocumentResponse.builder()
			.documentId(doc.getDocumentId())
			.status(doc.getStatus())
			.build();
	}

	/**
	 * 2) 관리자용: S3에 파일 업로드 후 요청에 연결
	 */
	@Override
	@Transactional
	public DocumentResponse attachFile(Long documentId, MultipartFile multipart) {
		// 1. 요청 엔티티 조회 (없으면 404)
		InsuranceDocument doc = provider.getById(documentId);

		// 2. S3 key 생성
		String key = "insurance/" + UUID.randomUUID() + "_" + multipart.getOriginalFilename();

		// 3. 메타데이터 준비 & 업로드
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(multipart.getContentType());
		metadata.setContentLength(multipart.getSize());
		try {
			amazonS3.putObject(bucket, key, multipart.getInputStream(), metadata);
		} catch (IOException e) {
			throw new FileUploadFailedException();  // 글로벌 예외 처리로 500 응답
		}

		// 4. File 엔티티 생성·저장
		File fileEntity = File.createFile(
			Category.INSURANCE,
			key,
			multipart.getOriginalFilename(),
			amazonS3.getUrl(bucket, key).toString(),
			bucket,
			multipart.getSize(),
			multipart.getContentType()
		);
		fileEntity = fileProvider.saveFile(fileEntity);

		// 5. 요청 엔티티에 연결 후 저장
		doc.attach(fileEntity);
		repo.save(doc);

		// 6. 응답 DTO
		return DocumentResponse.builder()
			.documentId(doc.getDocumentId())
			.status(doc.getStatus())
			.build();
	}

	/**
	 * 3) 단건 상태 조회
	 */
	@Override
	@Transactional(readOnly = true)
	public DocumentResponse status(Long documentId) {
		InsuranceDocument doc = provider.getById(documentId);
		return DocumentResponse.builder()
			.documentId(doc.getDocumentId())
			.status(doc.getStatus())
			.rejectionReason(doc.getRejectionReason())
			.downloadUrl(doc.getFile() != null ? doc.getFile().getUrl() : null)
			.build();
	}

	/**
	 * 4) 관리자용: 전체 목록 조회
	 */
	@Override
	@Transactional(readOnly = true)
	public List<DocumentResponse> listAll() {
		return provider.getAll().stream()
			.map(doc -> DocumentResponse.builder()
				.documentId(doc.getDocumentId())
				.status(doc.getStatus())
				.rejectionReason(doc.getRejectionReason())
				.downloadUrl(doc.getFile() != null ? doc.getFile().getUrl() : null)
				.build()
			)
			.collect(Collectors.toList());
	}

	/**
	 * 5) 승인/반려 처리
	 */
	@Override
	@Transactional
	public void approve(Long documentId, DocumentApproval dto) {
		InsuranceDocument doc = provider.getById(documentId);
		if (Boolean.TRUE.equals(dto.getApproved())) {
			doc.approve();
		} else {
			doc.reject(dto.getReason());
		}
		repo.save(doc);
	}

	/**
	 * 6) 환자·보호자용: S3에서 파일을 직접 스트리밍해서 바이트와 메타를 돌려줌
	 */
	@Override
	@Transactional(readOnly = true)
	public FileDownload downloadFile(Long documentId) {
		// ● 요청과 연관된 File 엔티티 조회
		InsuranceDocument doc = provider.getById(documentId);
		File file = doc.getFile();
		if (file == null) {
			throw new IllegalStateException("아직 파일이 첨부되지 않았습니다.");
		}

		// ● S3에서 객체(fetch)
		S3Object s3Object = amazonS3.getObject(file.getBucketName(), file.getFileName());
		try (S3ObjectInputStream is = s3Object.getObjectContent()) {
			byte[] data = is.readAllBytes();
			// ● DTO 로 래핑
			return FileDownload.builder()
				.data(data)
				.size(file.getFileSize())
				.contentType(file.getFileType())
				.originalName(file.getOriginalName())
				.build();
		} catch (IOException e) {
			throw new RuntimeException("파일 다운로드 중 오류가 발생했습니다.", e);
		}
	}
}
