package com.ssginc8.docto.doctor.entity;

import com.ssginc8.docto.doctor.dto.DoctorUpdateRequest;
import com.ssginc8.docto.global.base.BaseTimeEntity;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.repo.UserRepo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_doctor")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Doctor extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long doctorId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hospitalId", nullable = false)
	private Hospital hospital;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Specialization specialization;

	private Doctor(Hospital hospital, User user, Specialization specialization) {

		this.hospital = hospital;
		this.user = user;
		this.specialization = specialization;
	}


	public static Doctor create(Hospital hospital, User user, Specialization specialization) {
		return new Doctor(hospital, user, specialization);
	}

	public void updateFromDTO(DoctorUpdateRequest dto, UserRepo userRepo) {
		if (dto.getPassword() != null) {
			this.user.updatePassword(dto.getPassword());
		}

		if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
			if (userRepo.existsByEmail(dto.getEmail())) {
				throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
			}
			this.user.updateEmail(dto.getEmail());
		}

		if (dto.getSpecialization() != null) {
			this.specialization = dto.getSpecialization();  // 직접 필드 접근
		}
	}
}
