package com.ssginc8.docto.appointment.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.dto.AppointmentDailyCountResponse;
import com.ssginc8.docto.appointment.dto.AppointmentListResponse;
import com.ssginc8.docto.appointment.dto.AppointmentRequest;
import com.ssginc8.docto.appointment.dto.AppointmentResponse;
import com.ssginc8.docto.appointment.dto.AppointmentSearchCondition;
import com.ssginc8.docto.appointment.dto.TimeSlotDto;
import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.entity.AppointmentStatus;
import com.ssginc8.docto.appointment.entity.AppointmentType;
import com.ssginc8.docto.appointment.entity.PaymentType;
import com.ssginc8.docto.appointment.provider.AppointmentProvider;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.doctor.entity.DoctorSchedule;
import com.ssginc8.docto.doctor.provider.DoctorProvider;
import com.ssginc8.docto.appointment.validator.AppointmentValidator;
import com.ssginc8.docto.doctor.provider.DoctorScheduleProvider;
import com.ssginc8.docto.global.error.exception.appointmentException.IsSuspendedUserException;
import com.ssginc8.docto.global.error.exception.appointmentException.NotCanceledException;
import com.ssginc8.docto.global.error.exception.appointmentException.RoleNotFoundException;
import com.ssginc8.docto.global.event.appointment.AppointmentStatusChangedEvent;
import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.guardian.provider.GuardianProvider;
import com.ssginc8.docto.guardian.provider.PatientGuardianProvider;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.provider.HospitalProvider;
import com.ssginc8.docto.hospital.service.HospitalService;
import com.ssginc8.docto.notification.service.NotificationService;
import com.ssginc8.docto.patient.entity.Patient;
import com.ssginc8.docto.patient.provider.PatientProvider;
import com.ssginc8.docto.qna.dto.QaPostCreateRequest;
import com.ssginc8.docto.qna.provider.QaPostProvider;
import com.ssginc8.docto.qna.service.QaPostService;

import com.ssginc8.docto.user.entity.Role;

import com.ssginc8.docto.review.provider.ReviewProvider;

import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.provider.UserProvider;
import com.ssginc8.docto.user.service.UserService;
import com.ssginc8.docto.appointment.util.TimeSlotUtil;

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
	private final ReviewProvider reviewProvider;

	private final QaPostService qaPostService;
	private final UserService userService;
	private final HospitalService hospitalService;
	private final NotificationService notificationService;


	private final AppointmentValidator appointmentValidator;
	private final ApplicationEventPublisher applicationEventPublisher;

	/**
	 * 진료 예약 리스트 조회
	 * 검색 필터링 조건: hospitalId, doctorId, patientGuardianId
	 * 병원, 의사, 환자, 보호자 입장에서 검색 가능
	 */
	@Override
	public Page<AppointmentListResponse> getAppointmentList(Pageable pageable, AppointmentSearchCondition condition) {

		Page<Appointment> appointments = appointmentProvider.getAppointmentListByCondition(pageable, condition);

		return appointments.map(AppointmentListResponse::fromEntity);
	}

	/**
	 * Token의 정보로 Appointment List 가져오기
	 */
	@Override
	public Page<AppointmentListResponse> getAppointmentsByLoginUser(Pageable pageable, LocalDate date) {
		// 1. 로그인한 사용자 가져오기
		User loginUser = userService.getUserFromUuid();

		Page<Appointment> appointments;

		// 2. 역할에 따라 분기 처리
		switch (loginUser.getRole()) {
			case PATIENT:
				appointments =  appointmentProvider.getAppointmentsByPatient(loginUser.getUserId(), pageable);
				break;

			case GUARDIAN:
				appointments = appointmentProvider.getAppointmentsByGuardian(loginUser.getUserId(), pageable);
				break;

			case DOCTOR:
				return appointmentProvider.getAppointmentsByDoctor(loginUser.getUserId(), pageable)
					.map(AppointmentListResponse::fromEntity);

			case HOSPITAL_ADMIN:
				return appointmentProvider.getAppointmentsByHospital(loginUser.getUserId(), pageable, date)
					.map(AppointmentListResponse::fromEntity);
			default:
				throw new RoleNotFoundException();
		}

		// 리뷰가 작성된 appointmentId만 추출
		List<Long> appointmentIds = appointments.stream()
			.map(Appointment::getAppointmentId)
			.toList();

		Set<Long> reviewedAppointmentIds = reviewProvider.getReviewedAppointmentIds(appointmentIds);

		// 리뷰 여부 포함해서 DTO 변환
		return appointments.map(app ->
			AppointmentListResponse.fromEntity(
				app,
				reviewedAppointmentIds.contains(app.getAppointmentId())
			)
		);
	}

	/**
	 * 진료 예약의 단건 조회
	 */
	@Transactional(readOnly = true)
	@Override
	public AppointmentResponse getAppointmentDetail(Long appointmentId) {
		Appointment appointment = appointmentProvider.getAppointmentById(appointmentId);
		String qaPost = qaPostProvider.getQaPostByAppointment(appointment);

		return AppointmentResponse.fromEntity(appointment, qaPost);
	}

	/**
	 * 진료 예약 접수
	 * 접수 시 STATUS.REQUESTED
	 * 병원 확인 후 승인 -> CONFIRMED => 이 과정은 updateAppointmentStatus로 관리
	 * 			  거절 -> CANCELED
	 */
	@Transactional
	@Override
	public void requestAppointment(AppointmentRequest request) {
		// 1. 보호자 (user)와 환자 (patient) 조회
		User guardian = userProvider.getUserById(request.getUserId());

		if (guardian.getIsSuspended()) {
			throw new IsSuspendedUserException();
		}

		Patient patient = patientProvider.getActivePatient(request.getPatientId());
		
		// 2. 보호자-환자 관계 유효성 검사
		PatientGuardian patientGuardian = patientGuardianProvider.validateAndGetPatientGuardian(guardian, patient);
		
		// 3. 병원과 의사 조회
		Hospital hospital = hospitalProvider.getHospitalById(request.getHospitalId());
		Doctor doctor = doctorProvider.getDoctorById(request.getDoctorId());
		
		// 4. request 데이터 검증
		// 1) validate appointmentTime
		appointmentValidator.validateAppointmentTime(doctor, patientGuardian, request.getAppointmentTime());

		// 2) 수납 방식과 예약 타입 enum 값들 유효성 검사
		AppointmentType appointmentType = AppointmentType.from(request.getAppointmentType());
		PaymentType paymentType = PaymentType.from(request.getPaymentType());

		// 3) 중복 예약 방지
		// 동일 환자 예약 제한 (30분) : 다른 병원, 다른 의사라도
		appointmentValidator.validateDuplicateAppointment(patient, request.getAppointmentTime());

		// 한 병원의 한 의사가 30분 단위로 받을 수 있는 환자 수 제한
		appointmentValidator.validateDoctorSlotCapacity(doctor, request.getAppointmentTime());

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
			QaPostCreateRequest qaPostCreateRequest = QaPostCreateRequest.builder()
				.appointmentId(appointment.getAppointmentId())
				.content(request.getQuestion())
				.build();

			qaPostService.createQaPost(qaPostCreateRequest);
		}
	}

	/**
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

		applicationEventPublisher.publishEvent(new AppointmentStatusChangedEvent(appointment));

		return AppointmentResponse.fromEntity(appointment, qaContent);
	}

	/**
	 * 재예약: 기존 예약에서 예약 시간 변경
	 */
	@Transactional
	@Override
	public AppointmentResponse rescheduleAppointment(Long appointmentId, LocalDateTime newTime) {
		Appointment original = appointmentProvider.getAppointmentById(appointmentId);

		User guardian = original.getPatientGuardian().getUser();

		if (guardian.getIsSuspended()) {
			throw new IsSuspendedUserException();
		}

		original.changeStatus(AppointmentStatus.CANCELED);
		String qaContent = qaPostProvider.getQaPostByAppointment(original);

		appointmentValidator.validateAppointmentTime(original.getDoctor(), original.getPatientGuardian(), newTime);
		appointmentValidator.validateDuplicateAppointment(original.getPatientGuardian().getPatient(), newTime);
		appointmentValidator.validateDoctorSlotCapacity(original.getDoctor(), newTime);

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

	/**
	 * 예약 취소
	 */
	public void cancelAppointment(Long appointmentId) {
		Appointment appointment = appointmentProvider.getAppointmentById(appointmentId);
		User user = appointment.getPatientGuardian().getUser();

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime appointmentTime = appointment.getAppointmentTime();

		// 예약 상태 확인
		if (appointment.getStatus() != AppointmentStatus.REQUESTED &&
			appointment.getStatus() != AppointmentStatus.CONFIRMED) {
			throw new NotCanceledException();
		}

		// 상태 변경
		appointment.changeStatus(AppointmentStatus.CANCELED);
		appointmentProvider.save(appointment);

		// 1시간 이내 취소 -> 패널티 부여
		if (now.isAfter(appointmentTime.minusHours(1))) {
			user.addPenalty(1L);
			appointment.changeStatus(AppointmentStatus.NO_SHOW);
		}

		applicationEventPublisher.publishEvent(new AppointmentStatusChangedEvent(appointment));
	}

	@Override
	public List<TimeSlotDto> getAvailableTimeSlots(Long doctorId, Long patientId, LocalDate date) {
		Doctor doctor = doctorProvider.getDoctorById(doctorId);
		DoctorSchedule schedule = doctorScheduleProvider.getScheduleByDoctorAndDay(doctor, date.getDayOfWeek());
		Patient patient = patientProvider.getActivePatient(patientId);

		LocalDateTime baseDate = date.atStartOfDay();

		List<TimeSlotDto> slots = TimeSlotUtil.getTimeSlotsWithAvailability(
			schedule, doctor, baseDate, appointmentProvider
		);

		return slots.stream()
			.map(slot -> {
				// LocalDateTime으로 변환
				LocalDateTime slotStart = baseDate.with(slot.getStart());
				LocalDateTime slotEnd = baseDate.with(slot.getEnd());

				boolean overlaps =
					appointmentProvider.existsByPatientAndTimeRange(
						patientId,
						slotStart.minusMinutes(15),
						slotEnd.plusMinutes(15)
					);

				return new TimeSlotDto(slot.getStart(), slot.getEnd(), slot.isAvailable() && !overlaps);
			}).toList();
	}

	@Override
	public List<AppointmentDailyCountResponse> getDailyAppointmentCounts(LocalDate start, LocalDate end) {
		User loginUser = userService.getUserFromUuid();
		Hospital hospital = hospitalService.getByUserId(loginUser.getUserId());
		return appointmentProvider.countAppointmentsByDateRange(hospital.getHospitalId(), start, end);
	}
}
