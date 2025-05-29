package com.ssginc8.docto.guardian.service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.guardian.dto.GuardianStatusRequest;
import com.ssginc8.docto.guardian.dto.PatientSummaryResponse;
import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.guardian.entity.Status;
import com.ssginc8.docto.guardian.provider.GuardianProvider;
import com.ssginc8.docto.guardian.repo.PatientGuardianRepo;

import lombok.RequiredArgsConstructor;

/**
 * GuardianService 구현체
 * - provider를 통해 엔티티 조회
 * - 보호자-환자 권한 상태 관리
 */
@Service
@RequiredArgsConstructor
@Transactional
public class GuardianServiceImpl implements GuardianService {

	private final GuardianProvider guardianProvider;
	private final PatientGuardianRepo repository;

	/**
	 * 보호자 권한 상태를 수락 또는 거절로 변경합니다.
	 */
	@Override
	@Transactional
	public void updateStatus(Long requestId, String inviteCode, String statusStr) {
		PatientGuardian pg = guardianProvider.getById(requestId);

		// 1) 초대코드 일치 여부 검증
		if (!pg.getInviteCode().equals(inviteCode)) {
			throw new IllegalArgumentException("유효하지 않은 초대 코드입니다.");
		}

		// 2) 상태 변경
		Status newStatus = Status.valueOf(statusStr);
		pg.updateStatus(newStatus);
	}

	/**
	 * 보호자-환자 권한 매핑을 해제하는 메서드 (Soft Delete 방식)
	 *
	 * ✅ 주요 기능
	 * - guardianId(보호자)와 patientId(환자)를 기반으로
	 *   매핑(PatientGuardian)을 조회
	 * - 실제 DB에서 삭제하지 않고 `deletedAt` 필드를 현재 시간으로 설정하여
	 *   논리적으로 삭제된 것으로 처리 (Soft Delete)
	 *
	 * ✅ 예외 처리
	 * - 해당 guardianId/patientId 조합의 매핑이 존재하지 않거나
	 *   이미 Soft Delete된 경우 IllegalArgumentException 발생
	 *
	 * @param guardianId 보호자(User) 식별자
	 * @param patientId  환자(Patient) 식별자
	 */
	@Override
	public void deleteMapping(Long guardianId, Long patientId) {
		// 보호자-환자 매핑 엔티티 조회 (Soft Delete 제외 조건 포함)
		PatientGuardian pg = repository.findByUserIdAndPatientId(guardianId, patientId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매핑입니다."));

		// 실제 삭제하지 않고 deletedAt 필드에 시간 설정 (논리적 삭제 처리)
		pg.delete();
	}


	/**
	 * 현재 로그인한 보호자가 수락한 환자 목록을 조회합니다.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<PatientSummaryResponse> getAllAcceptedMappings() {
		return repository.findAllByStatus(Status.ACCEPTED).stream()
			.map(pg -> PatientSummaryResponse.of(
				pg.getPatient().getPatientId(),
				pg.getPatient().getUser().getName(),
				pg.getPatient().getResidentRegistrationNumber()
			))
			.collect(Collectors.toList());
	}

}
