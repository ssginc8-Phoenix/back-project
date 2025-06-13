package com.ssginc8.docto.medication.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ssginc8.docto.global.base.BaseTimeEntity;
import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.user.entity.User;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "patient_guardian_id", nullable = false)
	private Long patientGuardianId; // 보호자-환자 관계 ID

	@Column(nullable = false, length = 100)
	private String medicationName;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	@OneToMany(mappedBy = "medication", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<MedicationAlertTime> alertTimes = new ArrayList<>();

	public static MedicationInformation create(
		User user,
		Long patientGuardianId,
		String medicationName,
		LocalDate startDate,
		LocalDate endDate)
	{
		MedicationInformation info = new MedicationInformation();
		info.user = user;
		info.patientGuardianId = patientGuardianId;
		info.medicationName = medicationName;
		info.startDate = startDate;
		info.endDate = endDate;
		return info;
	}
}