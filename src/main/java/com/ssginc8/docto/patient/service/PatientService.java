package com.ssginc8.docto.patient.service;

import com.ssginc8.docto.patient.dto.PatientRequest;
import com.ssginc8.docto.patient.dto.PatientResponse;

import java.util.List;

/**
 * 환자 서비스 인터페이스
 * - 환자 관련 비즈니스 기능 정의
 * - 구현체는 PatientServiceImpl
 */
public interface PatientService {

	/**
	 * 환자 등록
	 * @param dto 환자 등록 요청 데이터
	 * @return 생성된 환자 응답
	 */
	PatientResponse createPatient(PatientRequest dto);

	/**
	 * 전체 환자 목록 조회
	 * @return 환자 응답 리스트
	 */
	List<PatientResponse> getAllPatients();

	/**
	 * 환자 삭제 처리 (soft delete)
	 * @param patientId 삭제할 환자 ID
	 */
	void deletePatient(Long patientId);
}