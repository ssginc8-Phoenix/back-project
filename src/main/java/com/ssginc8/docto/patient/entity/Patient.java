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

	@Column(name = "resident_registration_number", nullable = false, length = 20)
	private String residentRegistrationNumber;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Builder
	public Patient(User user, String residentRegistrationNumber) {
		this.user = user;
		this.residentRegistrationNumber = residentRegistrationNumber;
	}

	public void update(String newRRN) {
		this.residentRegistrationNumber = newRRN;
	}

	public void softDelete() {
		this.deletedAt = LocalDateTime.now();
	}
}