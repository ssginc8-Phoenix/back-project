package com.ssginc8.docto.appointment.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ssginc8.docto.appointment.dto.AppointmentDailyCountResponse;
import com.ssginc8.docto.appointment.dto.AppointmentListResponse;
import com.ssginc8.docto.appointment.dto.AppointmentRequest;
import com.ssginc8.docto.appointment.dto.AppointmentResponse;
import com.ssginc8.docto.appointment.dto.AppointmentSearchCondition;
import com.ssginc8.docto.appointment.dto.TimeSlotDto;
import com.ssginc8.docto.user.entity.Role;

public interface AppointmentService {

	// 예약 리스트 조회 (검색 조건)
	Page<AppointmentListResponse> getAppointmentList(Pageable pageable, AppointmentSearchCondition condition);

	/**
	 * Token의 정보로 Appointment List 가져오기
	 */
	Page<AppointmentListResponse> getAppointmentsByLoginUser(Pageable pageable, LocalDate date);


	// 예약 상세 조회
	AppointmentResponse getAppointmentDetail(Long appointmentId);

	// 예약 요청
	void requestAppointment(AppointmentRequest request);

	// 예약 상태 업데이트
	AppointmentResponse updateAppointmentStatus(Long appointmentId, String statusStr);

	// 재예약 (예약 시간만 변경)
	AppointmentResponse rescheduleAppointment(Long appointmentId, LocalDateTime newTime);

	List<AppointmentDailyCountResponse> getDailyAppointmentCounts(LocalDate start, LocalDate end);

	List<TimeSlotDto> getAvailableTimeSlots(Long doctorId, Long patientId, LocalDate date);

	void cancelAppointment(Long appointmentId);

	/**
	 * ( 예정된 ) 활성 예약 리스트 가져오기 : REQUESTED, CONFIRMED
	 */
	Page<AppointmentListResponse> getActiveAppointmentsByLoginUser(Pageable pageable, LocalDate date);

	/**
	 * ( 완료 / 취소된 ) 비활성 예약 리스트 가져오기 : COMPLETED, CANCELED, NO_SHOW
	 */
	Page<AppointmentListResponse> getInactiveAppointmentsByLoginUser(Pageable pageable, LocalDate date);
}
