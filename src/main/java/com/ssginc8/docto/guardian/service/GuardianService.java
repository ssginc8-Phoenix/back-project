package com.ssginc8.docto.guardian.service;

import java.util.List;

import com.ssginc8.docto.guardian.dto.GuardianStatusRequest;
import com.ssginc8.docto.guardian.dto.PatientSummaryResponse;

/**
 * 보호자 기능을 정의한 인터페이스
 * - 권한 수락/거절
 * - 보호자 관계 해제
 * - 보호자가 가진 환자 목록 조회
 */
public interface GuardianService {

	void updateStatus(Long requestId, GuardianStatusRequest request);

	void deleteMapping(Long guardianId, Long patientId);

	List<PatientSummaryResponse> getAllAcceptedMappings();
}