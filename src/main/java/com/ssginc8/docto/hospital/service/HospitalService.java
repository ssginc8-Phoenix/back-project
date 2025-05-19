package com.ssginc8.docto.hospital.service;


import java.util.List;

import com.ssginc8.docto.hospital.dto.HospitalListDTO;
import com.ssginc8.docto.hospital.dto.HospitalNameDTO;
import com.ssginc8.docto.hospital.dto.HospitalRequestDTO;
import com.ssginc8.docto.hospital.dto.HospitalScheduleDTO;
import com.ssginc8.docto.hospital.dto.HospitalWaitingDTO;
import com.ssginc8.docto.hospital.entity.Hospital;

public interface HospitalService {

		//병원 정보, 스케쥴 저장
		Long saveHospital(HospitalListDTO hospitalListDTO);

		//
		List<HospitalListDTO> getHospitalId(Long hospitalId);

		//위치정보 조회
		List<HospitalListDTO> getHospitalsWithinRadius(double lat, double lng, double radius);

		//병원 정보 수정하기
		HospitalRequestDTO updateHospital(Long hospitalId, HospitalRequestDTO hospitalRequestDTO);

		//병원 전체 정보 삭제
		void deleteHospital(Long hospitalId);

		//어드민용 병원 이름, 주소
		List<HospitalNameDTO> getHospitals();

		//스케쥴 등록
		List<HospitalScheduleDTO> saveSchedules(Long hospitalId, List<HospitalScheduleDTO> schedules);

		List<HospitalScheduleDTO> getSchedules(Long hospitalId);

		void updateHospitalSchedules(Long hospitalId, List<HospitalScheduleDTO> schedules);

		void deleteHospitalSchedules(Long hospitalId);

		Long saveHospitalWaiting(Long hospitalId,HospitalWaitingDTO hospitalWaitingDTO);

}

