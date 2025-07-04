package com.ssginc8.docto.appointment.dto;

import java.time.LocalDateTime;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.entity.AppointmentStatus;
import com.ssginc8.docto.appointment.entity.AppointmentType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppointmentListResponse {

	private Long appointmentId;

	private Long hospitalId;
	private Long doctorId;
	private Long patientGuardianId;

	private String hospitalName;
	private String doctorName;
	private String patientName;

	private LocalDateTime appointmentTime;
	private AppointmentType appointmentType;
	private AppointmentStatus status;

	private boolean hasReview;

	// 병원관리자, 의사는 hasReview 필요 없음
	public static AppointmentListResponse fromEntity(Appointment appointment) {
		return new AppointmentListResponse(
			appointment.getAppointmentId(),
			appointment.getHospital().getHospitalId(),
			appointment.getDoctor().getDoctorId(),
			appointment.getPatientGuardian().getPatientGuardianId(),
			appointment.getHospital().getName(),
			appointment.getDoctor().getUser().getName(),
			appointment.getPatientGuardian().getPatient().getUser().getName(),
			appointment.getAppointmentTime(),
			appointment.getAppointmentType(),
			appointment.getStatus(),
			false
		);
	}

	// 환자, 보호자 hasReview 필요
	public static AppointmentListResponse fromEntity(Appointment appointment, boolean hasReview) {
		return new AppointmentListResponse(
			appointment.getAppointmentId(),
			appointment.getHospital().getHospitalId(),
			appointment.getDoctor().getDoctorId(),
			appointment.getPatientGuardian().getPatientGuardianId(),
			appointment.getHospital().getName(),
			appointment.getDoctor().getUser().getName(),
			appointment.getPatientGuardian().getPatient().getUser().getName(),
			appointment.getAppointmentTime(),
			appointment.getAppointmentType(),
			appointment.getStatus(),
			hasReview
		);
	}
}
