package com.ssginc8.docto.appointment.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.entity.AppointmentStatus;
import com.ssginc8.docto.appointment.entity.AppointmentType;
import com.ssginc8.docto.appointment.entity.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AppointmentResponse {

	private Long appointmentId;

	private Long hospitalId;
	private Long doctorId;
	private Long patientId;

	private String hospitalName;
	private String doctorName;
	private String patientName;

	private String symptom;
	private String question;

	private LocalDateTime appointmentTime;
	private AppointmentType appointmentType;
	private PaymentType paymentType;
	private AppointmentStatus status;

	private String createdAt;

	public static AppointmentResponse fromEntity(Appointment appointment, String qaContent) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd(E) HH:mm", Locale.KOREAN);

		return AppointmentResponse.builder()
			.appointmentId(appointment.getAppointmentId())
			.hospitalId(appointment.getHospital().getHospitalId())
			.doctorId(appointment.getDoctor().getDoctorId())
			.patientId(appointment.getPatientGuardian().getPatient().getPatientId())
			.hospitalName(appointment.getHospital().getName())
			.doctorName(appointment.getDoctor().getUser().getName())
			.patientName(appointment.getPatientGuardian().getPatient().getUser().getName())
			.symptom(appointment.getSymptom())
			.question(qaContent)
			.appointmentTime(appointment.getAppointmentTime())
			.appointmentType(appointment.getAppointmentType())
			.paymentType(appointment.getPaymentType())
			.status(appointment.getStatus())
			.createdAt(appointment.getCreatedAt().format(formatter))
			.build();
	}
}
