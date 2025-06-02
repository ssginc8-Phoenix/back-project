package com.ssginc8.docto.doctor.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssginc8.docto.doctor.dto.DoctorSaveRequest;
import com.ssginc8.docto.doctor.dto.DoctorUpdateRequest;
import com.ssginc8.docto.doctor.dto.DoctorResponse;
import com.ssginc8.docto.doctor.dto.DoctorScheduleList;
import com.ssginc8.docto.doctor.dto.DoctorScheduleRequest;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.doctor.entity.DoctorSchedule;
import com.ssginc8.docto.doctor.repo.DoctorRepo;
import com.ssginc8.docto.doctor.repo.DoctorScheduleRepo;
import com.ssginc8.docto.doctor.service.DoctorService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Log4j2
public class DoctorController {
	private final DoctorService doctorService;


	/**
	 * 의사 등록
	 */
	@PostMapping("/doctors")
	public ResponseEntity<Long> createDoctor(@RequestBody DoctorSaveRequest doctorSaveRequest) {
		Long doctorId = doctorService.saveDoctor(doctorSaveRequest);
		return ResponseEntity.ok(doctorId);
	}


	/**
	 * 의사 전체 조회
	 */
	@GetMapping("/admin/doctors")
	public Page<DoctorResponse> getAllDoctors(Pageable pageable) {
		return doctorService.getDoctors(pageable);
	}

	/**
	 * 병원에 속한 의사 조회
	 * hospitalId = 22
	 */
	@GetMapping("/doctors")
 	public List<DoctorResponse> getDoctorsByHospital(@RequestParam Long hospitalId) {
		return doctorService.getDoctorsByHospital(hospitalId);
	}

	/**
	 * 의사 영업시간 등록
	 * 요일, 영업 시작 시간, 종료 시간, 점심 시작 시간, 종료 시간
	 */
	@PostMapping("/doctors/{doctorId}/schedules")
	public ResponseEntity<List<DoctorScheduleRequest>> saveDoctorSchedules(
		@PathVariable Long doctorId,
		@RequestBody List<DoctorScheduleRequest> doctorScheduleRequest) {
		doctorService.saveDoctorSchedule(doctorId, doctorScheduleRequest);
		return ResponseEntity.ok().build();
	}

	/**
	 * 의사 영업시간 조회
	 * DoctorId = 9
	 */
	@GetMapping("/doctors/{doctorId}/schedules")
	public ResponseEntity<List<DoctorScheduleList>> getDoctorSchedules(@PathVariable Long doctorId) {
		List<DoctorScheduleList> schedules = doctorService.getDoctorSchedule(doctorId);
		return ResponseEntity.ok(schedules);
	}

	/**
	 * 의사 영업시간 수정
	 * 요일, 영업 시작 시간, 종료 시간, 점심 시작 시간, 종료 시간
	 */
	@PatchMapping("/doctors/{doctorId}/schedules/{scheduleId}")
	public ResponseEntity<DoctorScheduleRequest> updateDoctorSchedule(
		@PathVariable Long doctorId,
		@PathVariable Long scheduleId,
		@RequestBody DoctorScheduleRequest doctorScheduleRequest
	) {
		DoctorScheduleRequest response = doctorService.updateDoctorSchedule(doctorId, scheduleId, doctorScheduleRequest);
		return ResponseEntity.ok(response); // 204 No Content
	}


	/**
	 * 의사 영업시간 삭제
	 * doctorId = 2, scheduleId = 2
	 */
	@DeleteMapping("/doctors/{doctorId}/schedules/{scheduleId}")
	public ResponseEntity<Void> deleteDoctorSchedule(
		@PathVariable Long doctorId,
		@PathVariable Long scheduleId
	) {


		doctorService.deleteDoctorSchedule(doctorId, scheduleId);
		return ResponseEntity.noContent().build(); // 204 No Content 반환
	}

	/**
	 * 의사가 30분 당 받을 수 있는 최대 환자 수 수정
	 * capacityPerHalfHour
	 */
	@PatchMapping("/{doctorId}/capacity")
	public ResponseEntity<Void> updateCapacityPerHalfHour(@PathVariable Long doctorId, @RequestBody Long capacityPerHalfHour) {
		return ResponseEntity.noContent().build();
	}


}
