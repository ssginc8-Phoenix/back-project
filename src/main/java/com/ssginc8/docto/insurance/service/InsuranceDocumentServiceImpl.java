// src/main/java/com/ssginc8/docto/insurance/service/InsuranceDocumentServiceImpl.java
package com.ssginc8.docto.insurance.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import lombok.RequiredArgsConstructor;

import com.ssginc8.docto.file.entity.File;
import com.ssginc8.docto.file.provider.FileProvider;
import com.ssginc8.docto.file.service.FileService;
import com.ssginc8.docto.insurance.entity.InsuranceDocument;
import com.ssginc8.docto.insurance.provider.InsuranceDocumentProvider;
import com.ssginc8.docto.insurance.repo.InsuranceDocumentRepo;
import com.ssginc8.docto.insurance.service.dto.DocumentRequestDTO;
import com.ssginc8.docto.insurance.service.dto.DocumentResponseDTO;
import com.ssginc8.docto.insurance.service.dto.DocumentApprovalDTO;

/**
 * InsuranceDocumentService 구현체
 * - 트랜잭션 경계 지정(@Transactional)
 * - 파일 업로드(S3) + 엔티티 저장 + 응답 DTO 변환 순으로 흐름 구성
 */
@Service
@RequiredArgsConstructor
public class InsuranceDocumentServiceImpl implements InsuranceDocumentService {
	private final FileService fileService;                     // S3 연동 서비스
	private final FileProvider fileProvider;                   // File 엔티티 저장/조회
	private final InsuranceDocumentRepo repo;            // Document 저장소
	private final InsuranceDocumentProvider provider;          // Document 조회 전용

	/**
	 * 1) S3에 파일 업로드
	 * 2) File 엔티티 생성 및 저장
	 * 3) InsuranceDocument 엔티티 생성 및 저장
	 * 4) DocumentResponseDTO 반환
	 */
	@Override
	@Transactional
	public DocumentResponseDTO request(DocumentRequestDTO dto) {
		// S3 업로드
		var upload = fileService.uploadImage(
			com.ssginc8.docto.file.service.dto.UploadFile.Command.builder()
				.file(dto.getFile())
				.category(com.ssginc8.docto.file.entity.Category.INSURANCE)
				.build()
		);

		// File 엔티티 생성 (정적 팩토리 메서드)
		File fileEntity = File.createFile(
			upload.getCategory(),
			upload.getFileName(),
			upload.getOriginalFileName(),
			upload.getUrl(),
			upload.getBucket(),
			upload.getFileSize(),
			upload.getFileType()
		);
		fileEntity = fileProvider.saveFile(fileEntity);

		// InsuranceDocument 엔티티 생성
		InsuranceDocument doc = InsuranceDocument.create(fileEntity);
		doc = repo.save(doc);

		return DocumentResponseDTO.builder()
			.documentId(doc.getDocumentId())
			.status(doc.getStatus())
			.build();
	}

	/** 단건 상태 조회 */
	@Override
	@Transactional(readOnly = true)
	public DocumentResponseDTO status(Long documentId) {
		InsuranceDocument doc = provider.getById(documentId);
		return DocumentResponseDTO.builder()
			.documentId(doc.getDocumentId())
			.status(doc.getStatus())
			.rejectionReason(doc.getRejectionReason())
			.build();
	}

	/** 전체 목록 조회 */
	@Override
	@Transactional(readOnly = true)
	public List<DocumentResponseDTO> listAll() {
		return provider.getAll().stream()
			.map(doc -> DocumentResponseDTO.builder()
				.documentId(doc.getDocumentId())
				.status(doc.getStatus())
				.rejectionReason(doc.getRejectionReason())
				.build())
			.collect(Collectors.toList());
	}

	/** 승인/반려 처리 */
	@Override
	@Transactional
	public void approve(Long documentId, DocumentApprovalDTO dto) {
		InsuranceDocument doc = provider.getById(documentId);
		if (Boolean.TRUE.equals(dto.getApproved())) {
			doc.approve();
		} else {
			doc.reject(dto.getReason());
		}
		repo.save(doc);
	}

	/**
	 * S3 URL을 Resource로 반환
	 * - UrlResource 사용
	 */
	@Override
	@Transactional(readOnly = true)
	public Resource download(Long documentId) {
		InsuranceDocument doc = provider.getById(documentId);
		String url = doc.getFile().getUrl();
		try {
			return new UrlResource(url);
		} catch (Exception e) {
			throw new RuntimeException("파일 다운로드 실패", e);
		}
	}
}
