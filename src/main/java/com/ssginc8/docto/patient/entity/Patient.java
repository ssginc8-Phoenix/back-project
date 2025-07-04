package com.ssginc8.docto.patient.entity;

import java.time.LocalDateTime;

import com.ssginc8.docto.global.base.BaseTimeEntity;
import com.ssginc8.docto.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 환자 정보를 나타내는 JPA 엔티티
 * - User 엔티티와 1:1 관계를 가짐
 * - 주민등록번호 및 삭제 시간 저장
 */
@Entity
@Table(name = "tbl_patient")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Patient extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long patientId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;


	@Column(name = "resident_registration_number", nullable = false)
	private String residentRegistrationNumber;

	// 생성자 대신 사용하는 정적 팩토리 메서드용 private 생성자
	private Patient(User user, String residentRegistrationNumber) {
		this.user = user;
		this.residentRegistrationNumber = residentRegistrationNumber;
	}

	/**
	 * 정적 팩토리 메서드 - 환자 엔티티 생성
	 */
	public static Patient create(User user, String residentRegistrationNumber) {
		return new Patient(user, residentRegistrationNumber);
	}

	/**
	 * 주민등록번호 수정
	 */
	public void update(String newRRN) {
		this.residentRegistrationNumber = newRRN;
	}
}
