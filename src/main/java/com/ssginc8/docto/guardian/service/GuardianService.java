package com.ssginc8.docto.guardian.service;

import java.util.List;

import com.ssginc8.docto.guardian.dto.GuardianInviteResponse;
import com.ssginc8.docto.guardian.dto.GuardianResponse;
import com.ssginc8.docto.guardian.dto.GuardianStatusRequest;
import com.ssginc8.docto.guardian.dto.PatientSummaryResponse;

/**
 * 보호자 기능을 정의한 인터페이스
 * - 권한 수락/거절
 * - 보호자 관계 해제
 * - 보호자가 가진 환자 목록 조회
 */
public interface GuardianService {

	/**
	 * @param requestId  요청 ID
	 * @param inviteCode 클라이언트가 보낸 초대코드 (검증용)
	 * @param status     새 상태 ("ACCEPTED" or "REJECTED")
	 */
	void updateStatus(Long requestId, String inviteCode, String status);

	void deleteMapping(Long guardianId, Long patientId);

	List<PatientSummaryResponse> getAllAcceptedMappings(Long guardianId);

	GuardianInviteResponse inviteGuardian(Long patientId, String guardianEmail);

	// 🔥 보호자 목록 조회
	List<GuardianResponse> getGuardiansByPatientId(Long patientId);

	void updateStatusByInviteCode(String inviteCode, String status);

	void deleteMappingByMappingId(Long mappingId);
}