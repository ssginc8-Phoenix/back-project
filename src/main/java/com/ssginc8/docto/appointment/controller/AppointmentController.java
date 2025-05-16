package com.ssginc8.docto.appointment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssginc8.docto.appointment.dto.AppointmentResponseDto;
import com.ssginc8.docto.appointment.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

	private final AppointmentService appointmentService;

	/* 진료 스케쥴 예약 (미리)
		URL: /api/v1/appointment/scheduled
		Method: POST
			BODY:
	*/
	@PostMapping("/scheduled")
	public ResponseEntity<?> scheduledAppointment() {

		return null;
	}

	/* 진료 당일 접수
		URL: /api/v1/appointment/walkin
		Method: POST
			BODY:
	*/
	@PostMapping("/walkin")
	public ResponseEntity<?> walkInAppointment() {

		return null;
	}

	/* 진료 예약 취소
		URL: /api/v1/appointment/{appointmentId}
		Method: DELETE
	*/
	@DeleteMapping("/{appointmentId}")
	public ResponseEntity<?> cancelAppointment(@PathVariable Long appointmentId) {
		return null;
	}

	/* 본인의 예약 리스트 조회
		URL: /api/v1/users/me/appointments
		Method: GET
	*/

	/* 본인의 예약 상세 내역 조회
		URL: /api/v1/users/me/appointment/{appointmentId}
		Method: GET
	*/
	@GetMapping("/{appointmentId}")
	public ResponseEntity<AppointmentResponseDto> getAppointmentDetail(@PathVariable Long appointmentId) {
		return ResponseEntity.ok(appointmentService.getAppointmentDetail(appointmentId));
	}

	/* 특정 예약의 대기순번 확인
		URL: /api/vi/appointment/{appointmentId}/waiting
		Method: GET
	*/

	/* 특정 병원의 대기 인원 확인
		URL: /api/v1/hospitals/{hospitalId}/waiting
		Method: GET
	*/
}
