package com.ssginc8.docto.appointment.provider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.dto.AppointmentSearchCondition;
import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.entity.AppointmentStatus;
import com.ssginc8.docto.appointment.repo.AppointmentRepo;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.global.error.exception.appointmentException.AppointmentNotFoundException;
import com.ssginc8.docto.guardian.entity.PatientGuardian;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor
@Log4j2
public class AppointmentProvider {

	private final AppointmentRepo appointmentRepo;

	/**
	 * 어드민 예약 리스트 조회
	 */
	@Transactional(readOnly = true)
	public Page<Appointment> getAppointmentListByCondition(Pageable pageable, AppointmentSearchCondition condition) {

		return appointmentRepo.findAllByCondition(condition, pageable);
	}

	/**
	 * appointmentId로 Appointment 단건 조회
	 */
	@Transactional(readOnly = true)
	public Appointment getAppointmentById(Long appointmentId) {
		return appointmentRepo.findById(appointmentId)
			.orElseThrow(AppointmentNotFoundException::new);
	}

	/**
	 * 중복되는 Appointment 존재 여부 조회
	 */
	@Transactional(readOnly = true)
	public boolean existsDuplicateAppointment(
		PatientGuardian patientGuardian, Doctor doctor, LocalDateTime appointmentTime
	) {
		return appointmentRepo.existsByPatientGuardianAndDoctorAndAppointmentTimeAndStatusNot(
			patientGuardian, doctor, appointmentTime, AppointmentStatus.CANCELED);
	}

	/**
	 * save Appointment
	 */
	@Transactional
	public Appointment save(Appointment appointment) {
		return appointmentRepo.save(appointment);
	}

	/**
	 * 시간 범위 내 환자의 Appointment 존재 여부
	 */
	@Transactional(readOnly = true)
	public boolean existsByPatientAndTimeRange(Long patientId, LocalDateTime start, LocalDateTime end) {
		return appointmentRepo.existsByPatientGuardian_Patient_PatientIdAndAppointmentTimeBetween(patientId, start, end);
	}

	/**
	 * 30분 단위의 시간 슬롯 안의 Appointment Count
	 */
	@Transactional(readOnly = true)
	public int countAppointmentsInSlot(Long doctorId, LocalDateTime slotStart, LocalDateTime slotEnd) {
		return appointmentRepo.countAppointmentsInSlot(doctorId, slotStart, slotEnd);
	}

	/**
	 * 환자의 Appointment 조회
	 */
	@Transactional(readOnly = true)
	public Page<Appointment> getAppointmentsByPatient(Long userId, Pageable pageable) {
		return appointmentRepo.findByPatientGuardian_Patient_User_UserIdOrderByAppointmentTimeAsc(userId, pageable);
	}

	/**
	 * 보호자의 Appointment 조회
	 */
	@Transactional(readOnly = true)
	public Page<Appointment> getAppointmentsByGuardian(Long userId, Pageable pageable) {
		return appointmentRepo.findByPatientGuardian_User_UserIdOrderByAppointmentTimeAsc(userId, pageable);
	}

	/**
	 * 의사의 Appointment 조회
	 */
	@Transactional(readOnly = true)
	public Page<Appointment> getAppointmentsByDoctor(Long userId, Pageable pageable) {
		return appointmentRepo.findByDoctor_User_UserIdOrderByAppointmentTimeAsc(userId, pageable);
	}

	/**
	 * 병원의 Appointment 조회
	 */
	@Transactional(readOnly = true)
	public Page<Appointment> getAppointmentsByHospital(Long userId, Pageable pageable, LocalDate date) {
		if (date != null) {
			// 00:00 ~ 23:59 까지 범위로 조회
			LocalDateTime startOfDay = date.atStartOfDay();
			LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

			log.info("날짜 별 appointment List 호출됨");

			return appointmentRepo.findByHospital_User_UserIdAndAppointmentTimeBetweenOrderByAppointmentTimeAsc(
				userId, startOfDay, endOfDay, pageable
			);
		} else {
			return appointmentRepo.findByHospital_User_UserIdOrderByAppointmentTimeAsc(userId, pageable);
		}
	}
}
