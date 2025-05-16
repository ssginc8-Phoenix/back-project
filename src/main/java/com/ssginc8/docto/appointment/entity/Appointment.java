package com.ssginc8.docto.appointment.entity;

import java.time.LocalDateTime;

import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.global.base.AppointmentStatus;
import com.ssginc8.docto.global.base.BaseTimeEntity;
import com.ssginc8.docto.guardian.entity.PatientGuardian;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_appointment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Appointment extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long appointmentId;

	@OneToOne
	@JoinColumn(name = "patientGuardianId", nullable = false)
	private PatientGuardian patientGuardian;

	@OneToOne
	@JoinColumn(name = "doctorId", nullable = false)
	private Doctor doctor;

	@Column(nullable = false)
	private LocalDateTime appointmentDate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AppointmentType appointmentType;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AppointmentStatus status;

	private Long queueNumber;	// 대기 순번
}
