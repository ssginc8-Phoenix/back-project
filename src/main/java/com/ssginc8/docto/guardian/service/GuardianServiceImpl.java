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
	public void updateStatus(Long requestId, GuardianStatusRequest request) {
		PatientGuardian pg = guardianProvider.getById(requestId);
		Status newStatus = Status.valueOf(request.getStatus());
		pg.updateStatus(newStatus);
	}

	/**
	 * 보호자-환자 권한 매핑을 해제합니다.
	 */
	@Override
	public void deleteMapping(Long guardianId, Long patientId) {
		repository.deleteByUser_UserIdAndPatient_PatientId(guardianId, patientId);
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