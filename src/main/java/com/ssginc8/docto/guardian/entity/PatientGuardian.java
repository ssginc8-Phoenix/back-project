package com.ssginc8.docto.guardian.entity;

import java.time.LocalDateTime;

import com.ssginc8.docto.patient.entity.Patient;
import com.ssginc8.docto.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 환자-보호자 관계를 나타내는 엔티티
 * - 보호자가 특정 환자에 대해 권한 요청을 하고
 *   수락/거절 여부와 시각 등을 저장
 */
@Entity
@Table(name = "tbl_patient_guardian")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PatientGuardian {

	/**
	 * 보호자 관계 식별자 (PK)
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long patientGuardianId;

	/**
	 * 보호자 사용자 (User)
	 */
	@ManyToOne
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	/**
	 * 보호받는 환자 (Patient)
	 */
	@ManyToOne
	@JoinColumn(name = "patientId", nullable = false)
	private Patient patient;

	/**
	 * 보호자 요청 상태 (PENDING, ACCEPTED, REJECTED)
	 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;

	/**
	 * 권한 요청이 발생한 시각
	 */
	@Column(nullable = false)
	private LocalDateTime invitedAt;

	/**
	 * 수락 또는 거절 응답한 시각 (선택적)
	 */
	private LocalDateTime respondedAt;

	// -------------------------------------
	// ✅ 정적 팩토리 메서드
	// -------------------------------------

	/**
	 * 보호자 권한 요청 엔티티 생성
	 * @param user 보호자
	 * @param patient 보호받는 환자
	 * @param invitedAt 위임 요청 시간
	 * @return 생성된 PatientGuardian 객체
	 */
	public static PatientGuardian create(User user, Patient patient, LocalDateTime invitedAt) {
		PatientGuardian pg = new PatientGuardian();
		pg.user = user;
		pg.patient = patient;
		pg.invitedAt = invitedAt;
		pg.status = Status.Pending;
		return pg;
	}

	// -------------------------------------
	// ✅ 상태 변경 로직
	// -------------------------------------

	/**
	 * 보호자 요청 상태 변경 (수락/거절)
	 * @param newStatus 새로운 상태 (ACCEPTED 또는 REJECTED)
	 */
	public void updateStatus(Status newStatus) {
		this.status = newStatus;
		this.respondedAt = LocalDateTime.now();
	}
}