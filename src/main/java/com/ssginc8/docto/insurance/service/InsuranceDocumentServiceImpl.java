// src/main/java/com/ssginc8/docto/insurance/service/InsuranceDocumentServiceImpl.java
package com.ssginc8.docto.insurance.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import com.ssginc8.docto.file.entity.File;
import com.ssginc8.docto.file.provider.FileProvider;
import com.ssginc8.docto.file.service.FileService;
import com.ssginc8.docto.insurance.entity.InsuranceDocument;
import com.ssginc8.docto.insurance.provider.InsuranceDocumentProvider;
import com.ssginc8.docto.insurance.repo.InsuranceDocumentRepo;
import com.ssginc8.docto.insurance.service.dto.DocumentResponseDTO;
import com.ssginc8.docto.insurance.service.dto.DocumentApprovalDTO;
import com.ssginc8.docto.insurance.service.dto.UserDocumentRequestDTO;

/**
 * InsuranceDocumentService 구현체
 *
 * • createRequest(): 환자·보호자용 요청 생성
 * • attachFile():   관리자용 파일 첨부
 * • status(), listAll(), approve(), download(): 공통/관리자 기능
 */
@Service
@RequiredArgsConstructor
public class InsuranceDocumentServiceImpl implements InsuranceDocumentService {

	private final FileService fileService;
	private final FileProvider fileProvider;
	private final InsuranceDocumentRepo repo;
	private final InsuranceDocumentProvider provider;

	// 1) 환자·보호자용: 파일 없이 요청만 생성
	@Override
	@Transactional
	public DocumentResponseDTO createRequest(UserDocumentRequestDTO dto) {
		// 정적 팩토리 메서드로 엔티티 생성
		InsuranceDocument doc = InsuranceDocument.createRequest(
			dto.getRequesterId(),
			dto.getNote()
		);

		// 저장
		doc = repo.save(doc);

		// DTO 반환
		return DocumentResponseDTO.builder()
			.documentId(doc.getDocumentId())
			.status(doc.getStatus())
			.build();
	}

	// 2) 관리자용: 기존 요청에 파일 첨부
	@Override
	@Transactional
	public DocumentResponseDTO attachFile(Long documentId, MultipartFile file) {
		// 기존 요청 조회
		InsuranceDocument doc = provider.getById(documentId);

		// S3에 파일 업로드
		var upload = fileService.uploadImage(
			com.ssginc8.docto.file.service.dto.UploadFile.Command.builder()
				.file(file)
				.category(com.ssginc8.docto.file.entity.Category.INSURANCE)
				.build()
		);

		// File 엔티티 생성·저장
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

		// 엔티티에 파일 연결
		doc.attach(fileEntity);

		// 저장 후 DTO 반환
		repo.save(doc);
		return DocumentResponseDTO.builder()
			.documentId(doc.getDocumentId())
			.status(doc.getStatus())
			.build();
	}

	// 3) 단건 상태 조회
	@Override
	@Transactional(readOnly = true)
	public DocumentResponseDTO status(Long documentId) {
		InsuranceDocument doc = provider.getById(documentId);
		return DocumentResponseDTO.builder()
			.documentId(doc.getDocumentId())
			.status(doc.getStatus())
			.rejectionReason(doc.getRejectionReason())
			.downloadUrl(doc.getFile() != null ? doc.getFile().getUrl() : null)
			.build();
	}

	// 4) 관리자용: 전체 요청 내역 조회
	@Override
	@Transactional(readOnly = true)
	public List<DocumentResponseDTO> listAll() {
		return provider.getAll().stream()
			.map(doc -> DocumentResponseDTO.builder()
				.documentId(doc.getDocumentId())
				.status(doc.getStatus())
				.rejectionReason(doc.getRejectionReason())
				.downloadUrl(doc.getFile() != null ? doc.getFile().getUrl() : null)
				.build()
			)
			.collect(Collectors.toList());
	}

	// 5) 승인/반려 처리
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

	// 6) 파일 다운로드용 Resource 반환
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
