package com.ssginc8.docto.hospital.controller;

import java.util.List;

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

import com.ssginc8.docto.hospital.dto.HospitalListDTO;
import com.ssginc8.docto.hospital.dto.HospitalNameDTO;
import com.ssginc8.docto.hospital.dto.HospitalRequestDTO;
import com.ssginc8.docto.hospital.dto.HospitalScheduleDTO;
import com.ssginc8.docto.hospital.dto.HospitalWaitingDTO;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.entity.HospitalSchedule;
import com.ssginc8.docto.hospital.service.HospitalService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Log4j2
public class HospitalController {


	private final HospitalService hospitalService;

	@GetMapping("hospitals/{hospitalId}")
	public ResponseEntity<List<HospitalListDTO>> getHospitalsId(@PathVariable Long hospitalId) {
		List<HospitalListDTO> list = hospitalService.getHospitalId(hospitalId);
		return ResponseEntity.ok(list);
	}

	@GetMapping("/hospitals/{hospitalId}/schedules")
	public ResponseEntity<List<HospitalScheduleDTO>> getSchedules(@PathVariable Long hospitalId) {
		List<HospitalScheduleDTO> list = hospitalService.getSchedules(hospitalId);
		return ResponseEntity.ok(list);
	}

	@PostMapping("/hospitals")
	public ResponseEntity<Long> registerHospital(@RequestBody HospitalListDTO dto) {
		Long hospitalId = hospitalService.saveHospital(dto);
		return ResponseEntity.ok(hospitalId);
	}

	@PostMapping("/hospitals/{hospitalId}/schedules")
	public ResponseEntity<List<HospitalScheduleDTO>> saveSchedules(
		@PathVariable Long hospitalId,
		@RequestBody List<HospitalScheduleDTO> schedules) {

		List<HospitalScheduleDTO> savedSchedules = hospitalService.saveSchedules(hospitalId, schedules);
		return ResponseEntity.ok(savedSchedules);
	}

	@GetMapping("/hospitals")
	public ResponseEntity<List<HospitalListDTO>> findHospitalsWithinRadius(
		@RequestParam("lat") double lat,
		@RequestParam("lng") double lng,
		@RequestParam("radius") double radius) {

		List<HospitalListDTO> list = hospitalService.getHospitalsWithinRadius(lat, lng, radius);
		return ResponseEntity.ok(list);
	}

	@PatchMapping("/hospitals/{hospitalId}")
	public ResponseEntity<HospitalRequestDTO> updateHospital(
		@PathVariable Long hospitalId,
		@RequestBody HospitalRequestDTO hospitalRequestDTO) {

		HospitalRequestDTO updatedHospital = hospitalService.updateHospital(hospitalId, hospitalRequestDTO);
		return ResponseEntity.ok(updatedHospital);
	}

	@DeleteMapping("/hospitals/{hospitalId}")
	public ResponseEntity<Void> deleteHospital(@PathVariable Long hospitalId) {
		hospitalService.deleteHospital(hospitalId);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/hospitals/{hospitalId}/schedules/{scheduleId}")
	public ResponseEntity<Void> deleteHospitalSchedules(@PathVariable Long hospitalId) {
		hospitalService.deleteHospitalSchedules(hospitalId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/admin/hospitals")
	public ResponseEntity<List<HospitalNameDTO>> getHospitals() {
		List<HospitalNameDTO> list = hospitalService.getHospitals();
		return ResponseEntity.ok(list);
	}

	@PatchMapping("/hospitals/{hospitalId}/schedules/{scheduleId}")
	public ResponseEntity<Void> updateHospitalSchedules(
		@PathVariable Long hospitalId,
		@RequestBody List<HospitalScheduleDTO> schedules) {
		hospitalService.updateHospitalSchedules(hospitalId, schedules);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/hospitals/{hospitalId}/waiting")
	public ResponseEntity<Long> updateHospitalWaiting(
		@PathVariable Long hospitalId,
		@RequestBody HospitalWaitingDTO hospitalWaitingDTO
	) {
		Long updatedHospitalId = hospitalService.saveHospitalWaiting(hospitalId, hospitalWaitingDTO);
		return ResponseEntity.ok(updatedHospitalId);
	}




}
