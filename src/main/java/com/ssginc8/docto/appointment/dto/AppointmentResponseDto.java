package com.ssginc8.docto.appointment.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ssginc8.docto.appointment.entity.AppointmentType;
import com.ssginc8.docto.appointment.entity.PaymentType;
import com.ssginc8.docto.global.base.AppointmentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppointmentResponseDto {

	private Long appointmentId;

	private Long hospitalId;
	private Long doctorId;
	private Long patientGuardianId;

	private String hospitalName;
	private String doctorName;
	private String patientName;

	private List<String> symptoms;
	private String question;

	private LocalDateTime appointmentTime;
	private AppointmentType appointmentType;
	private PaymentType paymentType;
	private AppointmentStatus status;
}
