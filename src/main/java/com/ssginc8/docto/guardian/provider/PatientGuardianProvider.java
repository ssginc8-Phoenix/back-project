package com.ssginc8.docto.guardian.provider;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.guardian.repo.PatientGuardianRepo;
import com.ssginc8.docto.patient.entity.Patient;
import com.ssginc8.docto.user.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PatientGuardianProvider {

	private final PatientGuardianRepo patientGuardianRepo;

	/*
	 *	보호자(user)와 환자(patient)의 관계가 유효한지 확인하고,
	 *  승인 상태(Accepted)인 patientGuardian 엔티티를 반환한다.
	 *  유효하지 않으면 예외를 던진다.
	 */
	public PatientGuardian validateAndGetPatientGuardian(User guardian, Patient patient) {
		Optional<PatientGuardian> optionalRelation = patientGuardianRepo.findByUserAndPatient(guardian, patient);

		if (optionalRelation.isEmpty()) {
			throw new IllegalArgumentException("보호자-환자 관계가 존재하지 않습니다.");
		}

		PatientGuardian relation = optionalRelation.get();

		switch (relation.getStatus()) {
			case PENDING -> throw new IllegalArgumentException("보호자가 환자 요청을 아직 수락하지 않았습니다.");
			case REJECTED -> throw new IllegalArgumentException("해당 보호자-환자 관계는 거절되었습니다.");
			case ACCEPTED -> {
				return relation;
			}
			default -> throw new IllegalArgumentException("알 수 없는 보호자-환자 관계 상태입니다.");
		}
	}

}
