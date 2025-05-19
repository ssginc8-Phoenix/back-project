package com.ssginc8.docto.appointment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ssginc8.docto.appointment.dto.AppointmentListResponse;
import com.ssginc8.docto.appointment.dto.AppointmentRequest;
import com.ssginc8.docto.appointment.dto.AppointmentResponse;
import com.ssginc8.docto.appointment.dto.AppointmentSearchCondition;
import com.ssginc8.docto.appointment.entity.Appointment;

public interface AppointmentService {

	// 예약 리스트 조회 (검색 조건)
	Page<AppointmentListResponse> getAppointmentList(Pageable pageable, AppointmentSearchCondition condition);

	// 예약 상세 조회
	AppointmentResponse getAppointmentDetail(Long appointmentId);

	// 예약 요청
	Appointment requestAppointment(AppointmentRequest request);
}
