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
import com.ssginc8.docto.global.error.exception.appointmentException.DuplicateAppointmentException;
import com.ssginc8.docto.global.error.exception.appointmentException.InvalidAppointmentTimeException;
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
	 * 병원, 의사, 환자, 보호자 입장에서 검색 가능
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
	public AppointmentResponse requestAppointment(AppointmentRequest request) {
		// 1. 보호자 (user)와 환자 (patient) 조회
		User guardian = userProvider.getUserById(request.getUserId());
		Patient patient = patientProvider.getPatientById(request.getPatientId());
		
		// 2. 보호자-환자 관계 유효성 검사
		PatientGuardian patientGuardian = patientGuardianProvider.validateAndGetPatientGuardian(guardian, patient);
		
		// 3. 병원과 의사 조회
		Hospital hospital = hospitalProvider.getHospitalById(request.getHospitalId());
		Doctor doctor = doctorProvider.getDoctorById(request.getDoctorId());
		
		// 4. request 데이터 검증
		// 1) validate appointmentTime
		validateAppointmentTime(doctor, patientGuardian, request.getAppointmentTime());

		// 2) 수납 방식과 예약 타입 enum 값들 유효성 검사
		AppointmentType appointmentType = AppointmentType.from(request.getAppointmentType());
		PaymentType paymentType = PaymentType.from(request.getPaymentType());

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

		String qaContent = qaPostProvider.getQaPostByAppointment(appointment);

		return AppointmentResponse.fromEntity(appointment, qaContent);
	}


	/*
	 * 진료 상태 업데이트
	 */
	@Transactional
	@Override
	public AppointmentResponse updateAppointmentStatus(Long appointmentId, String statusStr) {
		Appointment appointment = appointmentProvider.getAppointmentById(appointmentId);
		String qaContent = qaPostProvider.getQaPostByAppointment(appointment);

		AppointmentStatus newStatus = AppointmentStatus.from(statusStr);
		appointment.changeStatus(newStatus);

		appointmentProvider.save(appointment);

		return AppointmentResponse.fromEntity(appointment, qaContent);
	}

	/*
	 * 재예약: 기존 예약에서 예약 시간 변경
	 */
	@Transactional
	@Override
	public AppointmentResponse rescheduleAppointment(Long appointmentId, LocalDateTime newTime) {
		Appointment original = appointmentProvider.getAppointmentById(appointmentId);
		original.changeStatus(AppointmentStatus.CANCELED);
		String qaContent = qaPostProvider.getQaPostByAppointment(original);

		validateAppointmentTime(original.getDoctor(), original.getPatientGuardian(), newTime);

		Appointment newAppointment = Appointment.create(
			original.getPatientGuardian(),
			original.getHospital(),
			original.getDoctor(),
			newTime,
			original.getAppointmentType(),
			original.getSymptom(),
			AppointmentStatus.REQUESTED,
			original.getPaymentType()
		);

		appointmentProvider.save(newAppointment);
		return AppointmentResponse.fromEntity(newAppointment, qaContent);
	}

	// 예약시간 Validate
	private void validateAppointmentTime(
		Doctor doctor,
		PatientGuardian patientGuardian,
		LocalDateTime appointmentTime
	) {

		// 1) 과거 시간 불가
		if (appointmentTime.isBefore(LocalDateTime.now())) {
			throw new InvalidAppointmentTimeException();
		}

		// 2) 예약 시간과 의사 스케쥴 비교
		doctorScheduleProvider.validateDoctorSchedule(doctor, appointmentTime);

		// 3)중복 예약 체크
		if (appointmentProvider.existsDuplicateAppointment(patientGuardian, doctor, appointmentTime)) {
			throw new DuplicateAppointmentException();
		}
	}
}
