package com.ssginc8.docto.hospital.controller;

import java.util.List;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssginc8.docto.hospital.dto.HospitalRequest;
import com.ssginc8.docto.hospital.dto.HospitalResponse;
import com.ssginc8.docto.hospital.dto.HospitalScheduleRequest;
import com.ssginc8.docto.hospital.dto.HospitalScheduleResponse;
import com.ssginc8.docto.hospital.dto.HospitalUpdate;
import com.ssginc8.docto.hospital.dto.HospitalWaiting;
import com.ssginc8.docto.hospital.service.HospitalService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")

public class HospitalController {


	private final HospitalService hospitalService;

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
	@PostMapping("/hospitals")
	public ResponseEntity<Long> registerHospital(@Valid @RequestBody HospitalRequest hospitalRequest) {
		Long hospitalId = hospitalService.saveHospital(hospitalRequest);
		return ResponseEntity.ok(hospitalId);
	}

	/**
	 * 병원 정보 수정
	 *
	 */
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

		List<HospitalScheduleRequest> savedSchedules = hospitalService.saveSchedules(hospitalId, schedules);
		return ResponseEntity.ok(savedSchedules);
	}

	// @GetMapping("/hospitals")
	// public ResponseEntity<Page<HospitalNameDTO>> findHospitalsWithinRadius(//page로 바꾸기
	// 	@RequestParam("lat") double lat,
	// 	@RequestParam("lng") double lng,
	// 	@RequestParam("radius") double radius,
	// 	@PageableDefault(size = 10, sort = "name") Pageable pageable) {
	//
	// 	Page<HospitalNameDTO> page = hospitalService.getHospitalsWithinRadius(lat, lng, radius, pageable);
	// 	return ResponseEntity.ok(page);
	// }

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
	@PatchMapping("/hospitals/{hospitalId}/schedules/{scheduleId}")
	public ResponseEntity<Void> updateHospitalSchedule(
		@PathVariable Long hospitalId,
		@PathVariable Long scheduleId,
		@RequestBody HospitalScheduleRequest scheduleRequest
	) {
		// 병원 ID도 사실 검증용 외에 쓰지 않으면 생략해도 되지만, 일단 같이 넘기는 경우
		hospitalService.updateHospitalSchedule(hospitalId, scheduleId, scheduleRequest);
		return ResponseEntity.noContent().build();
	}

	/**
	 * 병원 웨이팅 등록
	 *
	 */
	@PatchMapping("/hospitals/{hospitalId}/waiting")
	public ResponseEntity<Long> saveHospitalWaiting(
		@PathVariable Long hospitalId,
		@RequestBody HospitalWaiting hospitalWaiting
	) {
		Long updatedHospitalId = hospitalService.saveHospitalWaiting(hospitalId, hospitalWaiting);
		return ResponseEntity.ok(updatedHospitalId);
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
	@PostMapping("/hospitals/{hospitalId}/waiting")
	public ResponseEntity<Long> updateHospitalWaiting(
		@PathVariable Long hospitalId,
		@RequestBody HospitalWaiting hospitalWaiting
	) {
		Long updatedId = hospitalService.updateHospitalWaiting(hospitalId, hospitalWaiting);
		return ResponseEntity.ok(updatedId);
	}




}
