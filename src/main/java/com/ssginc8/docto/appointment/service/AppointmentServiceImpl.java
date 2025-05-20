package com.ssginc8.docto.appointment.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.dto.AppointmentListResponse;
import com.ssginc8.docto.appointment.dto.AppointmentRequest;
import com.ssginc8.docto.appointment.dto.AppointmentResponse;
import com.ssginc8.docto.appointment.dto.AppointmentSearchCondition;
import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.entity.AppointmentStatus;
import com.ssginc8.docto.appointment.entity.AppointmentType;
import com.ssginc8.docto.appointment.entity.PaymentType;
import com.ssginc8.docto.appointment.provider.AppointmentProvider;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.doctor.provider.DoctorProvider;
import com.ssginc8.docto.doctor.provider.DoctorScheduleProvider;
import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.guardian.provider.PatientGuardianProvider;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.provider.HospitalProvider;
import com.ssginc8.docto.patient.entity.Patient;
import com.ssginc8.docto.patient.provider.PatientProvider;
import com.ssginc8.docto.qna.provider.QaPostProvider;
import com.ssginc8.docto.qna.service.QaPostService;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

	private final AppointmentProvider appointmentProvider;

	private final UserProvider userProvider;
	private final PatientProvider patientProvider;
	private final PatientGuardianProvider patientGuardianProvider;
	private final HospitalProvider hospitalProvider;
	private final DoctorProvider doctorProvider;
	private final DoctorScheduleProvider doctorScheduleProvider;
	private final QaPostProvider qaPostProvider;

	private final QaPostService qaPostService;

	/*
	 * 진료 예약 리스트 조회
	 * 검색 필터링 조건: hospitalId, doctorId, patientGuardianId
	 */
	@Override
	public Page<AppointmentListResponse> getAppointmentList(Pageable pageable, AppointmentSearchCondition condition) {

		Page<Appointment> appointments = appointmentProvider.getAppointmentListByCondition(pageable, condition);

		return appointments.map(AppointmentListResponse::fromEntity);
	}

	/*
	 * 진료 예약 단건 조회
	 */
	@Transactional(readOnly = true)
	@Override
	public AppointmentResponse getAppointmentDetail(Long appointmentId) {
		Appointment appointment = appointmentProvider.getAppointmentById(appointmentId);
		String qaPost = qaPostProvider.getQaPostByAppointment(appointment);

		return AppointmentResponse.fromEntity(appointment, qaPost);
	}

	/*
	 * 진료 예약 접수
	 */
	@Transactional
	@Override
	public Appointment requestAppointment(AppointmentRequest request) {
		// 1. 보호자 (user)와 환자 (patient) 조회
		User guardian = userProvider.getUserById(request.getUserId());
		Patient patient = patientProvider.getPatientById(request.getPatientId());
		
		// 2. 보호자-환자 관계 유효성 검사
		PatientGuardian patientGuardian = patientGuardianProvider.validateAndGetPatientGuardian(guardian, patient);
		
		// 3. 병원과 의사 조회
		Hospital hospital = hospitalProvider.getHospitalById(request.getHospitalId());
		Doctor doctor = doctorProvider.getDoctorById(request.getDoctorId());
		
		// 4. request 데이터 검증

		// 1) 예약 시간 과거 시간 입력 불가
		if(request.getAppointmentTime().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("과거 시간으로 예약할 수 없습니다.");
		}

		// 2) 예약 시간과 의사 스케쥴 비교
		doctorScheduleProvider.validateDoctorSchedule(doctor, request.getAppointmentTime());

		// 3) 증상 필수값 검사
		if (request.getSymptom() == null || request.getSymptom().trim().isEmpty()) {
			throw new IllegalArgumentException("증상은 필수 입력 항목입니다.");
		}

		// 4) 수납 방식과 예약 타입 enum 값들 유효성 검사
		AppointmentType appointmentType = AppointmentType.from(request.getAppointmentType());
		PaymentType paymentType = PaymentType.from(request.getPaymentType());

		// 5) 중복 예약 방지
		if (appointmentProvider.existsDuplicateAppointment(patientGuardian, doctor, request.getAppointmentTime())) {
			throw new IllegalStateException("이미 해당 시간에 예약이 존재합니다.");
		}

			// 동일 환자 예약 제한 (30분) : 다른 병원, 다른 의사라도

			// 한 병원의 한 의사가 30분 단위로 받을 수 있는 환자 수 제한

			// 예약 대기 리스트 및 자동 순번 배정 시스템 연동
		
		// 5. Appointment 엔티티 생성 및 저장
		Appointment appointment = Appointment.create(
			patientGuardian,
			hospital,
			doctor,
			request.getAppointmentTime(),
			appointmentType,
			request.getSymptom(),
			AppointmentStatus.REQUESTED,
			paymentType
		);

		appointmentProvider.save(appointment);

		// 6. 질문(QaPost) 저장
		if (request.getQuestion() != null && !request.getQuestion().isBlank()) {
			qaPostService.createQaPost(appointment, request.getQuestion());
		}

		return appointment;
	}
}
