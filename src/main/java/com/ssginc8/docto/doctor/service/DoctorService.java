package com.ssginc8.docto.doctor.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.ssginc8.docto.doctor.dto.DoctorProfileUpdateRequest;
import com.ssginc8.docto.doctor.dto.DoctorSaveRequest;
import com.ssginc8.docto.doctor.dto.DoctorResponse;
import com.ssginc8.docto.doctor.dto.DoctorScheduleList;
import com.ssginc8.docto.doctor.dto.DoctorScheduleRequest;

public interface DoctorService {

	//의사 등록
	Long saveDoctor(DoctorSaveRequest doctorSaveRequest);

	//의사 전체 조회
	Page<DoctorResponse> getDoctors(Pageable pageable);

	//병원마다 의사 조회
	List<DoctorResponse> getDoctorsByHospital(Long hospitalId);

	//의사스케쥴 등록
	List<DoctorScheduleRequest> saveDoctorSchedule(Long doctorId, List<DoctorScheduleRequest> doctorScheduleRequest);

	//의사스케쥴 조회
	List<DoctorScheduleList> getDoctorSchedule(Long doctorId);

	//의사스케쥴 수정
	DoctorScheduleRequest updateDoctorSchedule(Long doctorId, Long scheduleId, DoctorScheduleRequest doctorScheduleRequest);

	//의사스케쥴 삭제
	void deleteDoctorSchedule(Long doctorId, Long scheduleId);

	// 접수 가능한 환자 수 수정
	void updateCapacityPerHalfHour(Long doctorId, Long capacityPerHalfHour);

	DoctorResponse getDoctorInfoByUserId(Long doctorId);

	void updateDoctorProfile(Long doctorId, DoctorProfileUpdateRequest request, MultipartFile profileImage);
}
