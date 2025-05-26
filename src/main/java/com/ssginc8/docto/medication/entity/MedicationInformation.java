package com.ssginc8.docto.medication.entity;

import com.ssginc8.docto.global.base.BaseTimeEntity;

// guardian 완성 시 아래 import 사용 예정
// import com.ssginc8.docto.guardian.entity.PatientGuardian;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 약 정보 Entity
 *
 * - 특정 보호자-환자 관계(patientGuardian)를 기준으로 약 정보를 저장
 * - guardian 패키지 완성 전까지는 patientGuardianId(Long)로 보관
 */
@Entity
@Table(name = "tbl_medication_information")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MedicationInformation extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long medicationId;

	// guardian 완성 시 주석 해제 예정
    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_guardian_id", nullable = false)
    private PatientGuardian patientGuardian;
    */

	@Column(name = "patient_guardian_id", nullable = false)
	private Long patientGuardianId;

	@Column(nullable = false, length = 100)
	private String medicationName;

	public static MedicationInformation create(Long patientGuardianId, String medicationName) {
		MedicationInformation info = new MedicationInformation();
		info.patientGuardianId = patientGuardianId;
		info.medicationName = medicationName;
		return info;
	}
}
