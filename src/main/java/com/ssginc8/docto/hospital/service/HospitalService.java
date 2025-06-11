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
import com.ssginc8.docto.hospital.dto.HospitalWaitingRequest;
import com.ssginc8.docto.hospital.entity.Hospital;

public interface HospitalService {

		//위치정보 조회
		Page<HospitalResponse> getHospitalsWithinRadius(double lat, double lng, double radius, String query,
			 Pageable pageable);

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
		void saveSchedules(Long hospitalId, List<HospitalScheduleRequest> schedules);

		//영업시간 조회
		List<HospitalScheduleResponse> getSchedules(Long hospitalId);

		//영업시간 수정
		void updateHospitalSchedule(Long hospitalId, List<HospitalScheduleRequest> scheduleRequest);

		//영업시간 삭제
		void deleteHospitalSchedules(Long hospitalScheduleId);

		//병원 웨이팅 등록
		Long saveHospitalWaiting(Long hospitalId, HospitalWaitingRequest hospitalWaitingRequest);

		//병원 웨이팅 조회
		Long getHospitalWaiting(Long hospitalId);

		//병원 웨이팅 수정
		void updateHospitalWaiting(Long hospitalId, HospitalWaitingRequest hospitalWaiting);

		// 병원 리뷰
		Page<HospitalReviewResponse> getReviews(Long hospitalId, Pageable pageable);

		// 병원 검색
		Page<Hospital> searchHospitals(String query, Pageable pageable);

		// 로그인 사용자의 병원 정보 얻기
		HospitalResponse getHospitalByAdminId(Long userId);
}

