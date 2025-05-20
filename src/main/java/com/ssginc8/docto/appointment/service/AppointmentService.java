package com.ssginc8.docto.appointment.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ssginc8.docto.appointment.dto.AppointmentListResponse;
import com.ssginc8.docto.appointment.dto.AppointmentRequest;
import com.ssginc8.docto.appointment.dto.AppointmentResponse;
import com.ssginc8.docto.appointment.dto.AppointmentSearchCondition;

public interface AppointmentService {

	// 예약 리스트 조회 (검색 조건)
	Page<AppointmentListResponse> getAppointmentList(Pageable pageable, AppointmentSearchCondition condition);

	// 예약 상세 조회
	AppointmentResponse getAppointmentDetail(Long appointmentId);

	// 예약 요청
	AppointmentResponse requestAppointment(AppointmentRequest request);

	// 예약 상태 업데이트
	AppointmentResponse updateAppointmentStatus(Long appointmentId, String statusStr);

	// 재예약 (예약 시간만 변경)
	AppointmentResponse rescheduleAppointment(Long appointmentId, LocalDateTime newTime);
}
