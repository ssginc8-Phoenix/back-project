package com.ssginc8.docto.appointment.service;

import com.ssginc8.docto.appointment.dto.AppointmentResponseDto;

public interface AppointmentService {

	// 예약 상세 조회
	AppointmentResponseDto getAppointmentDetail(Long appointmentId);
}
