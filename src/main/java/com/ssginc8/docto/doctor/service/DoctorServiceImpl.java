package com.ssginc8.docto.doctor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssginc8.docto.doctor.dto.DoctorProfileUpdateRequest;
import com.ssginc8.docto.doctor.dto.DoctorSaveRequest;
import com.ssginc8.docto.doctor.dto.DoctorResponse;
import com.ssginc8.docto.doctor.dto.DoctorScheduleList;
import com.ssginc8.docto.doctor.dto.DoctorScheduleRequest;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.doctor.entity.DoctorSchedule;
import com.ssginc8.docto.doctor.provider.DoctorProvider;
import com.ssginc8.docto.doctor.provider.DoctorScheduleProvider;
import com.ssginc8.docto.file.entity.Category;
import com.ssginc8.docto.file.entity.File;
import com.ssginc8.docto.file.provider.FileProvider;
import com.ssginc8.docto.file.service.FileService;
import com.ssginc8.docto.file.service.dto.UploadFile;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.provider.HospitalProvider;

import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

	private final DoctorProvider doctorProvider;
	private final DoctorScheduleProvider doctorScheduleProvider;
	private final HospitalProvider hospitalProvider;
	private final UserProvider userProvider;
	private final FileProvider fileProvider;
	private final FileService fileService;

	/**
	 * 의사 등록
	 *
	 */
	@Override
	public Long saveDoctor(DoctorSaveRequest doctorSaveRequest) {

		User user = userProvider.getUserById(doctorSaveRequest.getUserId());

		doctorProvider.validateUserIsDoctor(user);
		doctorProvider.validateUserIsNotAlreadyDoctor(user);

		Hospital hospital = hospitalProvider.getHospitalById(doctorSaveRequest.getHospitalId());

		Doctor doctor = Doctor.create(hospital, doctorSaveRequest.getSpecialization(),user);

		return doctorProvider.saveDoctor(doctor).getDoctorId();
	}


	/**
	 * 전체 의사 조회
	 *
	 */
	@Override
	public Page<DoctorResponse> getDoctors(Pageable pageable) {
		Page<Doctor> doctors = doctorProvider.getAllDoctors(pageable);

		return doctors.map(doctor -> DoctorResponse.from(
			doctor,
			doctorProvider.getImageUrlOrNull(doctor)
		));
	}

	/**
	 * 해당 병원 의사 조회
	 *
	 */
	@Override
	public List<DoctorResponse> getDoctorsByHospital(Long hospitalId) {
		hospitalProvider.getHospitalById(hospitalId);
		List<Doctor> doctors = doctorProvider.getDoctorsByHospitalId(hospitalId);

		return doctors.stream()
			.map(d -> DoctorResponse.from(d, doctorProvider.getImageUrlOrNull(d)))
			.collect(Collectors.toList());
	}
	/**
	 * 의사 영업시간 등록
	 *
	 */
	@Override
	@Transactional
	public List<DoctorScheduleRequest> saveDoctorSchedule(
		Long doctorId,
		List<DoctorScheduleRequest> dtoList
	) {
		// 1) 의사 조회
		Doctor doctor = doctorProvider.getDoctorById(doctorId);

		// 2) 요청 목록 내 중복 요일 검증
		doctorScheduleProvider.validateNoDuplicateDay(dtoList);

		List<DoctorScheduleRequest> responses = new ArrayList<>();
		for (DoctorScheduleRequest dto : dtoList) {
			// 3) 필수 필드 Null 검증
			doctorScheduleProvider.validateRequiredFields(dto);
			// 4) 진료·점심시간 범위 검증
			doctorScheduleProvider.validateTimeRanges(dto);

			// 5) 엔티티 생성 및 저장
			DoctorSchedule schedule = DoctorSchedule.create(
				doctor,
				dto.getDayOfWeek(),
				dto.getStartTime(),
				dto.getEndTime(),
				dto.getLunchStart(),
				dto.getLunchEnd()
			);
			DoctorSchedule saved = doctorScheduleProvider.saveDoctorSchedule(schedule);
			responses.add(new DoctorScheduleRequest(saved));
		}
		return responses;
	}





	/**
	 * 의사 영업시간 조회
	 *
	 */
	@Override
	public List<DoctorScheduleList> getDoctorSchedule(Long doctorId) {
		doctorProvider.getDoctorById(doctorId);
		List<DoctorSchedule> schedules = doctorScheduleProvider.getSchedulesByDoctorId(doctorId);

		return schedules.stream()
			.map(DoctorScheduleList::new)
			.collect(Collectors.toList());
	}

	/**
	 * 의사 영업시간 수정
	 *
	 * @return
	 */
	@Override
	@Transactional
	public DoctorScheduleRequest updateDoctorSchedule(
		Long doctorId,
		Long scheduleId,
		DoctorScheduleRequest dto
	) {
		// 1) 의사 및 기존 스케줄 조회
		Doctor doctor = doctorProvider.getDoctorById(doctorId);
		DoctorSchedule existing = doctorScheduleProvider.getDoctorScheduleById(scheduleId);

		// 2) 소유 여부 검증
		doctorProvider.validateScheduleBelongsToDoctor(existing, doctor.getDoctorId());

		// 3) 필수 필드 Null 및 시간 범위 검증
		doctorScheduleProvider.validateRequiredFields(dto);
		doctorScheduleProvider.validateTimeRanges(dto);

		// 4) 엔티티 업데이트
		existing.updateDoctorSchedule(
			dto.getDayOfWeek(),
			dto.getStartTime(),
			dto.getEndTime(),
			dto.getLunchStart(),
			dto.getLunchEnd()
		);
		return dto;
	}


	/**
	 * 의사 영업시간 삭제
	 *
	 */
	@Override
	public void deleteDoctorSchedule(Long doctorId, Long scheduleId) {

		Doctor doctor = doctorProvider.getDoctorById(doctorId);
		DoctorSchedule schedule = doctorScheduleProvider.getDoctorScheduleById(scheduleId);

		doctorProvider.validateScheduleBelongsToDoctor(schedule, doctor.getDoctorId());
    
		doctorScheduleProvider.deleteDoctorSchedule(schedule);
	}

	/**
	 * 의사가 30분 당 받을 수 있는 최대 환자 수 수정
	 */
	@Override
	public void updateCapacityPerHalfHour(Long doctorId, Long capacityPerHalfHour) {
		Doctor doctor = doctorProvider.getDoctorById(doctorId);
		doctor.changeCapacityPerHalfHour(capacityPerHalfHour);
	}

	@Override
	public DoctorResponse getDoctorInfoByUserId(Long userId) {
		Doctor doctor = doctorProvider.getDoctorByUserId(userId);
		return DoctorResponse.from(doctor, doctorProvider.getImageUrlOrNull(doctor));
	}

	@Override
	public void updateDoctorProfile(Long doctorId, DoctorProfileUpdateRequest req, MultipartFile file) {
		Doctor doctor = doctorProvider.getDoctorById(doctorId);
		User user = doctor.getUser();

		File profileImage = null;

		if (file != null && !file.isEmpty()) {
			UploadFile.Result uploadResult = fileService.uploadImage(
				UploadFile.Command.builder()
					.file(file)
					.category(Category.USER) // 카테고리만 다르게
					.build()
			);

			File fileEntity = File.createFile(
				uploadResult.getCategory(),
				uploadResult.getFileName(),
				uploadResult.getOriginalFileName(),
				uploadResult.getUrl(),
				uploadResult.getBucket(),
				uploadResult.getFileSize(),
				uploadResult.getFileType()
			);

			profileImage = fileProvider.saveFile(fileEntity);
		}

		user.updatePhoneAndProfileImage(req.getPhone(), profileImage);
	}
}
