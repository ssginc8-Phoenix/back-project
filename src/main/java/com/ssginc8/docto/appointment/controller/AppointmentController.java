package com.ssginc8.docto.appointment.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
import com.ssginc8.docto.appointment.dto.RescheduleRequest;
import com.ssginc8.docto.appointment.dto.UpdateRequest;
import com.ssginc8.docto.appointment.service.AppointmentService;
import com.ssginc8.docto.user.entity.Role;
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

	/* ✅ 진료 예약 접수
	*	URL: /api/v1/appointments
	*	Method: POST
	*	BODY: AppointmentRequest
	*/
	@PostMapping("/appointments")
	public ResponseEntity<Void> requestAppointment(@RequestBody @Valid AppointmentRequest request) {
		appointmentService.requestAppointment(request);

		return ResponseEntity.ok().build();
	}


	/* ✅ 진료 예약 상태 업데이트
	 *	URL: /api/v1/appointments/{appointmentId}/status
	 *	Method: PATCH
	 *	BODY: 변화시킬려는 상태 str
	 */
	@PatchMapping("/appointments/{appointmentId}/status")
	public ResponseEntity<AppointmentResponse> updateAppointmentStatus(
		@PathVariable Long appointmentId,
		@RequestBody UpdateRequest request) {

		return ResponseEntity.ok(appointmentService.updateAppointmentStatus(appointmentId, request.getStatus()));
	}

	/* ✅ 재예약
	 *	URL: /api/v1/appointments/{appointmentId}/reschedule
	 *	Method: POST
	 */
	@PostMapping("/appointments/{appointmentId}/reschedule")
	public ResponseEntity<AppointmentResponse> rescheduleAppointment(
		@PathVariable Long appointmentId,
		@RequestBody RescheduleRequest request) {

		return ResponseEntity.ok(appointmentService.rescheduleAppointment(appointmentId, request.getNewTime()));
	}


	/* ✅ 예약 리스트 조회
		URL: /api/v1/appointments (ex: /api/v1/appointments?userId=1&page=0&size=10)
		Method: GET
	*/
	@GetMapping("/admin/appointments")
	public ResponseEntity<Page<AppointmentListResponse>> getAppointmentList(
		Pageable pageable,
		@ModelAttribute AppointmentSearchCondition condition
	) {
		Page<AppointmentListResponse> response = appointmentService.getAppointmentList(pageable, condition);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/users/me/appointments")
	public ResponseEntity<Page<AppointmentListResponse>> getMyAppointmentList(Pageable pageable) {
		Page<AppointmentListResponse> response = appointmentService.getAppointmentsByLoginUser(pageable);
		return ResponseEntity.ok(response);
	}

	/* ✅ 예약 상세 내역 조회
		URL: /api/v1/appointments/{appointmentId}
		Method: GET
	*/
	@GetMapping("/appointments/{appointmentId}")
	public ResponseEntity<AppointmentResponse> getAppointmentDetail(@PathVariable Long appointmentId) {
		return ResponseEntity.ok(appointmentService.getAppointmentDetail(appointmentId));
	}

}
