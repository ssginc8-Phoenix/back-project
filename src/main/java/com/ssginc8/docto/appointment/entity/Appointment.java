package com.ssginc8.docto.appointment.entity;

import java.time.LocalDateTime;

import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.global.base.BaseTimeEntity;
import com.ssginc8.docto.global.error.exception.appointmentException.AppointmentCanceledModificationNotAllowedException;
import com.ssginc8.docto.global.error.exception.appointmentException.AppointmentCompletedModificationNotAllowedException;
import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.user.entity.User;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patientGuardianId", nullable = false)
	private PatientGuardian patientGuardian;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hospitalId", nullable = false)
	private Hospital hospital;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctorId", nullable = false)
	private Doctor doctor;

	@Column(nullable = false)
	private LocalDateTime appointmentTime;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AppointmentType appointmentType;

	@Column(nullable = false)
	private String symptom;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AppointmentStatus status;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentType paymentType;

	private Long queueNumber;    // 대기 순번

	public Appointment(
		PatientGuardian patientGuardian,
		Hospital hospital,
		Doctor doctor,
		LocalDateTime appointmentTime,
		AppointmentType appointmentType,
		String symptom,
		AppointmentStatus status,
		PaymentType paymentType,
		Long queueNumber) {

		this.patientGuardian = patientGuardian;
		this.hospital = hospital;
		this.doctor = doctor;
		this.appointmentTime = appointmentTime;
		this.appointmentType = appointmentType;
		this.symptom = symptom;
		this.status = status;
		this.paymentType = paymentType;
		this.queueNumber = queueNumber;
	}

	public static Appointment create(
		PatientGuardian patientGuardian,
		Hospital hospital,
		Doctor doctor,
		LocalDateTime appointmentTime,
		AppointmentType appointmentType,
		String symptom,
		AppointmentStatus status,
		PaymentType paymentType) {

		return new Appointment(
			patientGuardian, hospital, doctor, appointmentTime,
			appointmentType, symptom, status, paymentType, null);
	}

	public void changeStatus(AppointmentStatus newStatus) {
		if (this.status == AppointmentStatus.COMPLETED) {
			throw new AppointmentCompletedModificationNotAllowedException();
		}

		if (this.status == AppointmentStatus.CANCELED) {
			throw new AppointmentCanceledModificationNotAllowedException();
		}

		this.status = newStatus;
	}

	public User getGuardian() {
		return this.patientGuardian.getUser();
	}

	public String getPatientName() {
		return this.patientGuardian.getPatient().getUser().getName();
	}

	public String getHospitalName() {
		return this.hospital.getName();
	}

	public String getDoctorName() {
		return this.doctor.getUser().getName();
	}

	public String getGuardianName() {
		return this.patientGuardian.getUser().getName();
	}

	public String getGuardianEmail() {
		return this.patientGuardian.getUser().getEmail();
	}
}
