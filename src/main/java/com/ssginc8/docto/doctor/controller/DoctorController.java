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
@RequestMapping("/api/v1/doctors")
@Log4j2
public class DoctorController {
	private final DoctorService doctorService;
	private final DoctorScheduleRepo doctorScheduleRepo;
	private final DoctorRepo doctorRepo;

	/**
	 * 의사 등록
	 * 병원 ID, 전공, 이름, 비밀번호, 이메일, 로그인타입, 역할, 정지여부, UUID
	 */
	@PostMapping
	public ResponseEntity<Long> createDoctor(@RequestBody DoctorSaveRequest doctorSaveRequest) {
		Long doctorId = doctorService.saveDoctor(doctorSaveRequest);
		return ResponseEntity.ok(doctorId);
	}

	/**
	 * 의사 정보 수정
	 * 이메일, 비밀번호, 전공 변경 가능
	 */
	@PatchMapping("/{doctorId}")
	public ResponseEntity<DoctorUpdateRequest> updateDoctor(@PathVariable Long doctorId, @RequestBody DoctorUpdateRequest doctorUpdateRequest) {

		DoctorUpdateRequest updateDoctor = doctorService.updateDoctor(doctorId, doctorUpdateRequest);
		return ResponseEntity.ok(updateDoctor);
	}

	/**
	 * 의사 전체 조회
	 */
	@GetMapping
	public Page<DoctorResponse> getAllDoctors(Pageable pageable) {
		return doctorService.getDoctors(pageable);
	}

	/**
	 * 병원에 속한 의사 조회
	 * hospitalId = 22
	 */
	@GetMapping("/hospitalId")
 	public List<DoctorResponse> getDoctorsByHospital(@RequestParam Long hospitalId) {
		return doctorService.getDoctorsByHospital(hospitalId);
	}

	/**
	 * 의사 영업시간 등록
	 * 요일, 영업 시작 시간, 종료 시간, 점심 시작 시간, 종료 시간
	 */
	@PostMapping("/{doctorId}/schedules")
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
	@GetMapping("/{doctorId}/schedules")
	public ResponseEntity<List<DoctorScheduleList>> getDoctorSchedules(@PathVariable Long doctorId) {
		List<DoctorScheduleList> schedules = doctorService.getDoctorSchedule(doctorId);
		return ResponseEntity.ok(schedules);
	}

	/**
	 * 의사 영업시간 수정
	 * 요일, 영업 시작 시간, 종료 시간, 점심 시작 시간, 종료 시간
	 */
	@PatchMapping("/{doctorId}/schedules/{scheduleId}")
	public ResponseEntity<DoctorScheduleRequest> updateDoctorSchedule(
		@PathVariable Long doctorId,
		@PathVariable Long scheduleId,
		@RequestBody DoctorScheduleRequest doctorScheduleRequest
	) {
		DoctorScheduleRequest updatedSchedule = doctorService.updateDoctorSchedule(doctorId, scheduleId, doctorScheduleRequest);
		return ResponseEntity.ok(updatedSchedule);
	}

	/**
	 * 의사 영업시간 삭제
	 * doctorId = 2, scheduleId = 2
	 */
	@DeleteMapping("/{doctorId}/schedules/{scheduleId}")
	public ResponseEntity<Void> deleteDoctorSchedule(
		@PathVariable Long doctorId,
		@PathVariable Long scheduleId
	) {
		// 의사 존재 여부 확인 (필요 시)
		Doctor doctor = doctorRepo.findById(doctorId)
			.orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + doctorId));

		// 스케줄 존재 여부 확인
		DoctorSchedule schedule = doctorScheduleRepo.findById(scheduleId)
			.orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + scheduleId));

		// 스케줄이 해당 의사의 것인지 확인
		if (!schedule.getDoctor().getDoctorId().equals(doctor.getDoctorId())) {
			throw new IllegalArgumentException("Schedule does not belong to the doctor");
		}

		// 삭제
		doctorScheduleRepo.delete(schedule);

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
