package com.ssginc8.docto.hospital.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import com.ssginc8.docto.hospital.dto.HospitalRequest;
import com.ssginc8.docto.hospital.dto.HospitalResponse;
import com.ssginc8.docto.hospital.dto.HospitalReviewResponse;
import com.ssginc8.docto.hospital.dto.HospitalScheduleRequest;
import com.ssginc8.docto.hospital.dto.HospitalScheduleResponse;
import com.ssginc8.docto.hospital.dto.HospitalUpdate;
import com.ssginc8.docto.hospital.dto.HospitalWaiting;

public interface HospitalService {

		//위치정보 조회
		//Page<HospitalNameDTO> getHospitalsWithinRadius(double lat, double lng, double radius, Pageable pageable);

		//병원 정보
		Long saveHospital(HospitalRequest HospitalRequest);

		//병원 상세 조회
		HospitalResponse getHospitalId(Long hospitalId);

		//병원 정보 수정하기
		Long updateHospital(Long hospitalId, HospitalUpdate hospitalUpdate  );

		//병원 전체 정보 삭제
		void deleteHospital(Long hospitalId);

		//어드민용 병원 이름, 주소
		Page<HospitalResponse> getHospitals(Pageable pageable);

		//영업시간 등록
		List<HospitalScheduleRequest> saveSchedules(Long hospitalId, List<HospitalScheduleRequest> schedules);

		//영업시간 조회
		List<HospitalScheduleResponse> getSchedules(Long hospitalId);

		//영업시간 수정
		void updateHospitalSchedule(Long hospitalId, Long scheduleId, HospitalScheduleRequest scheduleRequest);

		//영업시간 삭제
		void deleteHospitalSchedules(Long hospitalScheduleId);

		//병원 웨이팅 등록
		Long saveHospitalWaiting(Long hospitalId, HospitalWaiting hospitalWaiting);

		//병원 웨이팅 조회
		Long getHospitalWaiting(Long hospitalId);

		//병원 웨이팅 수정
		Long updateHospitalWaiting(Long hospitalId, HospitalWaiting hospitalWaiting);

		Page<HospitalReviewResponse> getReviews(Long hospitalId, Pageable pageable);
}

