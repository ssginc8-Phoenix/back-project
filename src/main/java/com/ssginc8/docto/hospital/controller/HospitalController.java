package com.ssginc8.docto.hospital.controller;

import java.util.List;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssginc8.docto.hospital.dto.HospitalRequest;
import com.ssginc8.docto.hospital.dto.HospitalResponse;
import com.ssginc8.docto.hospital.dto.HospitalReviewResponse;
import com.ssginc8.docto.hospital.dto.HospitalScheduleRequest;
import com.ssginc8.docto.hospital.dto.HospitalScheduleResponse;
import com.ssginc8.docto.hospital.dto.HospitalUpdate;
import com.ssginc8.docto.hospital.dto.HospitalWaitingResponse;
import com.ssginc8.docto.hospital.dto.HospitalWaitingRequest;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.service.HospitalService;
import com.ssginc8.docto.review.service.ReviewService;
import com.ssginc8.docto.user.service.UserService;
import com.ssginc8.docto.user.service.dto.UserInfo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")

public class HospitalController {


	private final HospitalService hospitalService;
	private final ReviewService reviewService;
	private final UserService userService;



	/**
	 * 로그인 사용자의 병원 정보
	 *
	 */
	@GetMapping("/hospitals/me")
	public ResponseEntity<HospitalResponse> getMyHospitalInfo() {
		UserInfo.Response userInfo = userService.getMyInfo();
		Long userId = userInfo.userId;  // UserInfo.Response 에 ID 필드가 있다고 가정
		HospitalResponse hospital = hospitalService.getHospitalByAdminId(userId);
		return ResponseEntity.ok(hospital);
	}

	/**
	 * 병원 검색(전체 모드)
	 *
	 */
	@GetMapping("/hospitals/search")
	public ResponseEntity<Page<HospitalResponse>> searchHospitals(
		@RequestParam(required = false) String query,
		@PageableDefault(size = 10, sort = "name") Pageable pageable
	) {
		Page<HospitalResponse> hospitals = hospitalService.searchHospitals(query, pageable);
		return ResponseEntity.ok(hospitals);
	}


	/**
	 * 병원아이디에 대한 상세 조회
	 *
	 */
	@GetMapping("hospitals/{hospitalId}")
	public ResponseEntity<HospitalResponse> getHospitalsId(@PathVariable Long hospitalId) {

		return ResponseEntity.ok(hospitalService.getHospitalId(hospitalId));
	}

	/**
	 * 병원 등록
	 *
	 */
	@PostMapping( "/hospitals")
	public ResponseEntity<Long> registerHospital(
		@ModelAttribute HospitalRequest hospitalRequest // MultipartFile 포함
	) {
		UserInfo.Response userInfo = userService.getMyInfo();
		Long hospitalId = hospitalService.saveHospital(userInfo.userId, hospitalRequest);
		return ResponseEntity.ok(hospitalId);
	}

	/**
	 * 병원 정보 수정
	 *
	 */
	@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
	@PatchMapping("/hospitals/{hospitalId}")
	public ResponseEntity<Long> updateHospital(
		@PathVariable Long hospitalId,
		@RequestBody HospitalUpdate hospitalUpdate) {

		Long updatedHospital = hospitalService.updateHospital(hospitalId, hospitalUpdate);
		return ResponseEntity.ok(updatedHospital);
	}

	/**
	 * 병원 정보 삭제
	 *
	 */
	@DeleteMapping("/hospitals/{hospitalId}")
	public ResponseEntity<Void> deleteHospital(@PathVariable Long hospitalId) {
		hospitalService.deleteHospital(hospitalId);
		return ResponseEntity.noContent().build();
	}

	/**
	 * 병원 전체 리스트(어드민)
	 *
	 */
	@GetMapping("/admin/hospitals")
	public ResponseEntity<Page<HospitalResponse>> getHospitals(Pageable pageable) {
		Page<HospitalResponse> page = hospitalService.getHospitals(pageable);
		return ResponseEntity.ok(page);
	}

	/**
	 * 병원 영업시간 조회
	 *
	 */
	@GetMapping("/hospitals/{hospitalId}/schedules")
	public ResponseEntity<List<HospitalScheduleResponse>> getSchedules(@PathVariable Long hospitalId) {
		List<HospitalScheduleResponse> list = hospitalService.getSchedules(hospitalId);
		return ResponseEntity.ok(list);
	}


	/**
	 * 병원 영업시간 등록
	 *
	 */
	@PostMapping("/hospitals/{hospitalId}/schedules")
	public ResponseEntity<List<HospitalScheduleRequest>> saveSchedules(
		@PathVariable Long hospitalId,
		@RequestBody List<HospitalScheduleRequest> schedules) {

		hospitalService.saveSchedules(hospitalId, schedules);
		return ResponseEntity.ok().build();
	}
	/**
	 * 병원 위치 기반 검색(내 주변)
	 */
	@CrossOrigin(origins = "http://localhost:5173")
	@GetMapping("/hospitals")
	public ResponseEntity<Page<HospitalResponse>> findHospitalsWithinRadius(
		@RequestParam("lat") double lat,
		@RequestParam("lng") double lng,
		@RequestParam("radius") double radius,
		@RequestParam(value = "query", required = false) String query,
		@PageableDefault(size = 10, sort = "name") Pageable pageable) {

		Page<HospitalResponse> page = hospitalService.getHospitalsWithinRadius(
			lat, lng, radius, query,pageable
		);
		return ResponseEntity.ok(page);
	}

	/**
	 * 병원 스케쥴 삭제
	 *
	 */
	@DeleteMapping("/hospitals/{hospitalId}/schedules/{scheduleId}")
	public ResponseEntity<Void> deleteHospitalSchedules(@PathVariable Long scheduleId) {
		hospitalService.deleteHospitalSchedules(scheduleId);
		return ResponseEntity.noContent().build();
	}

	/**
	 * 병원 영업시간 수정
	 *
	 */
	@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
	@PatchMapping("/hospitals/{hospitalId}/schedules")
	public ResponseEntity<Void> updateHospitalSchedules(
		@PathVariable Long hospitalId,
		@RequestBody List<HospitalScheduleRequest> scheduleRequests
	) {
		hospitalService.updateHospitalSchedule(hospitalId, scheduleRequests);
		return ResponseEntity.noContent().build();
	}

	/**
	 * 병원 웨이팅 등록
	 *
	 */
	@PostMapping("/hospitals/{hospitalId}/waiting")
	public ResponseEntity<HospitalWaitingResponse> saveHospitalWaiting(
		@PathVariable Long hospitalId,
		@RequestBody HospitalWaitingRequest request
	) {
		hospitalService.saveHospitalWaiting(hospitalId, request);
		return ResponseEntity.ok(new HospitalWaitingResponse(request.getWaiting()));
	}


	/**
	 * 병원 웨이팅 조회
	 *
	 */
	@GetMapping("/hospitals/{hospitalId}/waiting")
	public ResponseEntity<Long> getHospitalWaiting(@PathVariable Long hospitalId) {
		Long waiting = hospitalService.getHospitalWaiting(hospitalId);
		return ResponseEntity.ok(waiting);
	}

	/**
	 * 병원 웨이팅 수정
	 *
	 */
	@PatchMapping("/hospitals/{hospitalId}/waiting")
	public ResponseEntity<Long> updateHospitalWaiting(
		@PathVariable Long hospitalId,
		@RequestBody HospitalWaitingRequest hospitalWaiting
	) {
		hospitalService.updateHospitalWaiting(hospitalId, hospitalWaiting);
		return ResponseEntity.noContent().build();
	}

	// 한 병원에 대한 리뷰 조회
	@GetMapping("/hospitals/{hospitalId}/reviews")
	public ResponseEntity<Page<HospitalReviewResponse>> getAllReviews(@PathVariable Long hospitalId, Pageable pageable
	) {Page<HospitalReviewResponse> page = hospitalService.getReviews(hospitalId,pageable);
		return ResponseEntity.ok(page);
	}
}
