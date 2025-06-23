// src/main/java/com/ssginc8/docto/insurance/service/InsuranceDocumentServiceImpl.java
package com.ssginc8.docto.insurance.service;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.provider.HospitalProvider;
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

	private final InsuranceDocumentRepo      repo;
	private final InsuranceDocumentProvider  provider;
	private final HospitalProvider           hospitalProvider;
	private final AmazonS3                   amazonS3;
	private final FileProvider               fileProvider;

	/** S3 버킷 이름을 application.yml 에서 주입 */
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	// 1) 환자·보호자용: 요청 생성
	@Override
	@Transactional
	public DocumentResponse createRequest(UserDocumentRequest dto) {
		// 발급 Hospital 조회 (없으면 404)
		Hospital hosp = hospitalProvider.getHospitalById(dto.getHospitalId());

		// 엔티티 생성 (file=null, status=REQUESTED)
		InsuranceDocument doc = InsuranceDocument.createRequest(
			dto.getRequesterId(),
			hosp,
			dto.getNote()
		);
		repo.save(doc);

		return DocumentResponse.builder()
			.documentId(doc.getDocumentId())
			.status(doc.getStatus())
			.hospitalId(hosp.getHospitalId())
			.hospitalName(hosp.getName())
			.build();
	}

	// 1-2) 환자·보호자용: 본인 요청 페이징 조회
	@Override
	@Transactional(readOnly = true)
	public Page<DocumentResponse> listUserRequests(Long requesterId, Pageable pageable) {
		return repo.findAllByRequesterId(requesterId, pageable)
			.map(doc -> DocumentResponse.builder()
				.documentId(doc.getDocumentId())
				.status(doc.getStatus())
				.rejectionReason(doc.getRejectionReason())
				.downloadUrl(doc.getFile() != null ? doc.getFile().getUrl() : null)
				.hospitalId(doc.getHospital().getHospitalId())
				.hospitalName(doc.getHospital().getName())
				.build()
			);
	}

	// 2) 관리자용: 파일 첨부
	@Override
	@Transactional
	public DocumentResponse attachFile(Long documentId, MultipartFile multipart) {
		InsuranceDocument doc = provider.getById(documentId);

		// S3 키 생성
		String key = "insurance/" + UUID.randomUUID() + "_" + multipart.getOriginalFilename();
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentType(multipart.getContentType());
		meta.setContentLength(multipart.getSize());

		try {
			amazonS3.putObject(bucket, key, multipart.getInputStream(), meta);
		} catch (IOException e) {
			throw new FileUploadFailedException();
		}

		// File 엔티티 생성/저장
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

		// 요청에 파일 연결 후 저장
		doc.attach(fileEntity);
		repo.save(doc);

		return DocumentResponse.builder()
			.documentId(doc.getDocumentId())
			.status(doc.getStatus())
			.hospitalId(doc.getHospital().getHospitalId())
			.hospitalName(doc.getHospital().getName())
			.build();
	}

	// 3) 단건 상태 조회
	@Override
	@Transactional(readOnly = true)
	public DocumentResponse status(Long documentId) {
		InsuranceDocument doc = provider.getById(documentId);
		return DocumentResponse.builder()
			.documentId(doc.getDocumentId())
			.status(doc.getStatus())
			.rejectionReason(doc.getRejectionReason())
			.downloadUrl(doc.getFile() != null ? doc.getFile().getUrl() : null)
			.hospitalId(doc.getHospital().getHospitalId())
			.hospitalName(doc.getHospital().getName())
			.build();
	}

	// 4) 관리자용: 전체 요청 페이징 조회
	@Override
	@Transactional(readOnly = true)
	public Page<DocumentResponse> listAll(Pageable pageable) {
		return provider.getAll(pageable)  // provider 에도 Page<InsuranceDocument> getAll(Pageable) 추가
			.map(doc -> DocumentResponse.builder()
				.documentId(doc.getDocumentId())
				.status(doc.getStatus())
				.rejectionReason(doc.getRejectionReason())
				.downloadUrl(doc.getFile() != null ? doc.getFile().getUrl() : null)
				.hospitalId(doc.getHospital().getHospitalId())
				.hospitalName(doc.getHospital().getName())
				.build()
			);
	}

	// 5) 승인/반려 처리
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

	// 6) S3 파일 다운로드
	@Override
	@Transactional(readOnly = true)
	public FileDownload downloadFile(Long documentId) {
		InsuranceDocument doc = provider.getById(documentId);
		File file = doc.getFile();
		if (file == null) {
			throw new IllegalStateException("아직 파일이 첨부되지 않았습니다.");
		}

		S3Object s3Object = amazonS3.getObject(file.getBucketName(), file.getFileName());
		try (S3ObjectInputStream is = s3Object.getObjectContent()) {
			byte[] data = is.readAllBytes();
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
