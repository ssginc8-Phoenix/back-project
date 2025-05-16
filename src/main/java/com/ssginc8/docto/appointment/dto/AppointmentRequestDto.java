package com.ssginc8.docto.appointment.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ssginc8.docto.appointment.entity.AppointmentType;
import com.ssginc8.docto.appointment.entity.PaymentType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppointmentRequestDto {

	private Long hospitalId;
	private Long doctorId;
	private Long patientGuardianId;

	private List<String> symptoms;
	private String question;

	private LocalDateTime appointmentTime;
	private AppointmentType appointmentType;
	private PaymentType paymentType;
}
