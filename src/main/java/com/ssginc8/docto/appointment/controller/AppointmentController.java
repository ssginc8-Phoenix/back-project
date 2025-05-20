package com.ssginc8.docto.appointment.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssginc8.docto.appointment.dto.AppointmentListResponse;
import com.ssginc8.docto.appointment.dto.AppointmentRequest;
import com.ssginc8.docto.appointment.dto.AppointmentResponse;
import com.ssginc8.docto.appointment.dto.AppointmentSearchCondition;
import com.ssginc8.docto.appointment.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

	private final AppointmentService appointmentService;

	/* ✅ 진료 예약 접수
	*	URL: /api/v1/appointments
	*	Method: POST
	*	BODY: AppointmentRequest
	*/
	@PostMapping()
	public ResponseEntity<AppointmentResponse> requestAppointment(@RequestBody AppointmentRequest request) {

		return ResponseEntity.ok(appointmentService.requestAppointment(request));
	}


	/* ✅ 진료 예약 상태 업데이트
	 *	URL: /api/v1/appointments/{appointmentId}/status
	 *	Method: PATCH
	 *	BODY: 변화시킬려는 상태 str
	 */
	@PatchMapping("/{appointmentId}/status")
	public ResponseEntity<AppointmentResponse> updateAppointmentStatus(@PathVariable Long appointmentId, @RequestBody String statusStr) {

		return ResponseEntity.ok(appointmentService.updateAppointmentStatus(appointmentId, statusStr));
	}


	/* 진료 예약 취소
		URL: /api/v1/appointments/{appointmentId}
		Method: DELETE
	*/
	@DeleteMapping("/{appointmentId}")
	public ResponseEntity<?> cancelAppointment(@PathVariable Long appointmentId) {

		return ResponseEntity.ok().build();
	}


	/* ✅ 예약 리스트 조회
		URL: /api/v1/appointments (ex: /api/v1/appointments?userId=1&page=0&size=10)
		Method: GET
	*/
	@GetMapping
	public ResponseEntity<Page<AppointmentListResponse>> getAppointmentList(
		Pageable pageable,
		@ModelAttribute AppointmentSearchCondition condition
	) {
		Page<AppointmentListResponse> response = appointmentService.getAppointmentList(pageable, condition);

		return ResponseEntity.ok(response);
	}

	/* ✅ 예약 상세 내역 조회
		URL: /api/v1/appointments/{appointmentId}
		Method: GET
	*/
	@GetMapping("/{appointmentId}")
	public ResponseEntity<AppointmentResponse> getAppointmentDetail(@PathVariable Long appointmentId) {
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
