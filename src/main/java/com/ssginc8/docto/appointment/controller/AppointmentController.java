package com.ssginc8.docto.appointment.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssginc8.docto.appointment.dto.AppointmentDailyCountResponse;
import com.ssginc8.docto.appointment.dto.AppointmentListResponse;
import com.ssginc8.docto.appointment.dto.AppointmentRequest;
import com.ssginc8.docto.appointment.dto.AppointmentResponse;
import com.ssginc8.docto.appointment.dto.AppointmentSearchCondition;
import com.ssginc8.docto.appointment.dto.RescheduleRequest;
import com.ssginc8.docto.appointment.dto.TimeSlotDto;
import com.ssginc8.docto.appointment.dto.UpdateRequest;
import com.ssginc8.docto.appointment.service.AppointmentService;
import com.ssginc8.docto.hospital.service.HospitalService;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AppointmentController {

	private final AppointmentService appointmentService;
	private final UserService userService;
	private final HospitalService hospitalService;
	/**
	 * ✅ 진료 예약 접수
	 * URL : /api/v1/appointments
	 * Method : POST
	 * Body: AppointmentRequest
	 */
	@PostMapping("/appointments")
	public ResponseEntity<Void> requestAppointment(@RequestBody @Valid AppointmentRequest request) {
		appointmentService.requestAppointment(request);

		return ResponseEntity.ok().build();
	}


	/**
	 * ✅ 진료 예약 상태 업데이트
	 * URL: /api/v1/appointments/{appointmentId}/status
	 * Method: PATCH
	 * BODY: 변화시킬려는 상태 str
	 */
	@PatchMapping("/appointments/{appointmentId}/status")
	public ResponseEntity<AppointmentResponse> updateAppointmentStatus(
		@PathVariable Long appointmentId,
		@RequestBody UpdateRequest request) {

		return ResponseEntity.ok(appointmentService.updateAppointmentStatus(appointmentId, request.getStatus()));
	}

	/**
	 * ✅ 진료 예약 취소
	 * URL: /api/v1/appointments/{appointmentId}/cancel
	 * Method: PATCH
	 */
	@PatchMapping("/appointments/{appointmentId}/cancel")
	public ResponseEntity<Void> cancelAppointment(@PathVariable Long appointmentId) {
		appointmentService.cancelAppointment(appointmentId);

		return ResponseEntity.noContent().build();
	}

	/**
	 * ✅ 재예약
	 * URL: /api/v1/appointments/{appointmentId}/reschedule
	 * Method: POST
	 */
	@PostMapping("/appointments/{appointmentId}/reschedule")
	public ResponseEntity<AppointmentResponse> rescheduleAppointment(
		@PathVariable Long appointmentId,
		@RequestBody RescheduleRequest request) {

		return ResponseEntity.ok(appointmentService.rescheduleAppointment(appointmentId, request.getNewTimeAsLocalDateTime()));
	}


	/**
	 * ✅ 예약 리스트 조회
	 * URL: /api/v1/admin/appointments (ex: /api/v1/admin/appointments?userId=1&page=0&size=10)
	 * Method: GET
	 */
	@GetMapping("/admin/appointments")
	public ResponseEntity<Page<AppointmentListResponse>> getAppointmentList(
		Pageable pageable,
		@ModelAttribute AppointmentSearchCondition condition
	) {
		Page<AppointmentListResponse> response = appointmentService.getAppointmentList(pageable, condition);

		return ResponseEntity.ok(response);
	}

	/**
	 * ✅ 예약 리스트 조회 (로그인한 유저의)
	 * URL: /users/me/appointments
	 * Method: GET
	 */
	@GetMapping("/users/me/appointments")
	public ResponseEntity<Page<AppointmentListResponse>> getMyAppointmentList(
		Pageable pageable,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	) {
		Page<AppointmentListResponse> response = appointmentService.getAppointmentsByLoginUser(pageable, date);
		return ResponseEntity.ok(response);
	}

	/**
	 * ✅ ( 예정된 ) 활성 예약 리스트 조회 (로그인한 유저의)
	 * URL: /users/me/appointments/active
	 * Method: GET
	 */
	@GetMapping("/users/me/appointments/active")
	public ResponseEntity<Page<AppointmentListResponse>> getMyActiveAppointmentList(
		Pageable pageable,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	) {
		Page<AppointmentListResponse> response = appointmentService.getActiveAppointmentsByLoginUser(pageable, date);
		return ResponseEntity.ok(response);
	}

	/**
	 * ✅ ( 완료 / 취소된 ) 비활성 예약 리스트 조회 (로그인한 유저의)
	 * URL: /users/me/appointments/inactive
	 * Method: GET
	 */
	@GetMapping("/users/me/appointments/inactive")
	public ResponseEntity<Page<AppointmentListResponse>> getMyInactiveAppointmentList(
		Pageable pageable,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	) {
		Page<AppointmentListResponse> response = appointmentService.getInactiveAppointmentsByLoginUser(pageable, date);
		return ResponseEntity.ok(response);
	}

	/**
	 * ✅ 예약 상세 내역 조회
	 * URL: /api/v1/appointments/{appointmentId}
	 * Method: GET
	 */
	@GetMapping("/appointments/{appointmentId}")
	public ResponseEntity<AppointmentResponse> getAppointmentDetail(@PathVariable Long appointmentId) {
		return ResponseEntity.ok(appointmentService.getAppointmentDetail(appointmentId));
	}

	/**
	 * ✅ 예약 가능한 시간 슬롯 조회
	 * URL: /api/v1/appointments/available-time-slots
	 * Method: GET
	 */
	@GetMapping("/appointments/available-time-slots")
	public ResponseEntity<List<TimeSlotDto>> getAvailableTimeSlots(
		@RequestParam Long doctorId,
		@RequestParam Long patientId,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	) {
		List<TimeSlotDto> slots = appointmentService.getAvailableTimeSlots(doctorId, patientId, date);
		return ResponseEntity.ok(slots);
	}

	/**
	 * ✅ 일일 진료 수 조회
	 */

	@GetMapping("/appointments/daily-count")
	public ResponseEntity<List<AppointmentDailyCountResponse>> getDailyAppointmentCounts(
		@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
		@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
	) {
		List<AppointmentDailyCountResponse> response = appointmentService.getDailyAppointmentCounts(start, end);
		return ResponseEntity.ok(response);
	}
}
