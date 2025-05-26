package com.ssginc8.docto.appointment.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppointmentRequest {

	private Long hospitalId;
	private Long doctorId;
	private Long userId;	// 보호자
	private Long patientId;	// 환자

	@NotBlank(message = "증상은 필수 입력 항목입니다.")
	private String symptom;
	private String question;

	private LocalDateTime appointmentTime;
	private String appointmentType;
	private String paymentType;
}
