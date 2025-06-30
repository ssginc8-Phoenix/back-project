package com.ssginc8.docto.payment.service.dto;

import java.time.LocalDateTime;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.payment.entity.PaymentRequest;
import com.ssginc8.docto.payment.entity.RequestStatus;

import lombok.Getter;

public class GetPaymentHistory {
	@Getter
	public static class Response {
		private final Long appointmentId;
		private final String hospitalName;
		private final String doctorName;
		private final String patientName;
		private final String guardianName;
		private final String guardianEmail;
		private final Long paymentAmount;
		private final RequestStatus requestStatus;
		private final LocalDateTime appointmentTime;

		private Response(Long appointmentId, String hospitalName, String doctorName, String patientName,
			String guardianName, String guardianEmail, Long paymentAmount,
			RequestStatus requestStatus, LocalDateTime appointmentTime) {
			this.appointmentId = appointmentId;
			this.hospitalName = hospitalName;
			this.doctorName = doctorName;
			this.patientName = patientName;
			this.guardianName = guardianName;
			this.guardianEmail = guardianEmail;
			this.paymentAmount = paymentAmount;
			this.requestStatus = requestStatus;
			this.appointmentTime = appointmentTime;
		}
	}

	public static Response toResponse(Appointment appointment, PaymentRequest paymentRequest) {
		return new Response(appointment.getAppointmentId(), appointment.getHospitalName(),
			appointment.getDoctorName(),
			appointment.getPatientName(), appointment.getGuardianName(), appointment.getGuardianEmail(),
			paymentRequest.getAmount(),
			paymentRequest.getStatus(),
			appointment.getAppointmentTime());
	}
}