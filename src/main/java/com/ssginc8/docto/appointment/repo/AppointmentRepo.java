package com.ssginc8.docto.appointment.repo;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.entity.AppointmentStatus;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.guardian.entity.PatientGuardian;

public interface AppointmentRepo
	extends JpaRepository<Appointment, Long>, QuerydslPredicateExecutor<Appointment>, QAppointmentRepo {

	@EntityGraph(attributePaths = {
		"hospital",
		"doctor.user",
		"patientGuardian.patient.user"
	})
	Optional<Appointment> findById(Long appointmentId);

	boolean existsByPatientGuardianAndDoctorAndAppointmentTimeAndStatusNot(
		PatientGuardian patientGuardian,
		Doctor doctor,
		LocalDateTime appointmentTime,
		AppointmentStatus status
	);

	boolean existsByPatientGuardian_Patient_PatientIdAndAppointmentTimeBetween(Long patientId, LocalDateTime start, LocalDateTime end);

	@Query("SELECT COUNT(a) FROM Appointment a " +
		"WHERE a.doctor.doctorId = :doctorId " +
		"AND a.appointmentTime >= :slotStart " +
		"AND a.appointmentTime < :slotEnd " +
		"AND a.status = 'CONFIRMED'")
	int countAppointmentsInSlot(
		@Param("doctorId") Long doctorId,
		@Param("slotStart") LocalDateTime slotStart,
		@Param("slotEnd") LocalDateTime slotEnd
	);

	/**
	 * 로그인 한 유저의 예약 리스트 가져오기
	 */
	@EntityGraph(attributePaths = {
		"hospital.user",
		"doctor.user",
		"patientGuardian.patient.user"
	})
	Page<Appointment> findByPatientGuardian_Patient_User_UserIdOrderByAppointmentTimeAsc(Long userId, Pageable pageable);

	@EntityGraph(attributePaths = {
		"hospital.user",
		"doctor.user",
		"patientGuardian.patient.user"
	})
	Page<Appointment> findByPatientGuardian_User_UserIdOrderByAppointmentTimeAsc(Long userId, Pageable pageable);

	@EntityGraph(attributePaths = {
		"hospital.user",
		"doctor.user",
		"patientGuardian.patient.user"
	})
	Page<Appointment> findByDoctor_User_UserIdOrderByAppointmentTimeAsc(Long userId, Pageable pageable);

	@EntityGraph(attributePaths = {
		"hospital.user",
		"doctor.user",
		"patientGuardian.patient.user"
	})
	Page<Appointment> findByHospital_User_UserIdOrderByAppointmentTimeAsc(Long userId, Pageable pageable);
}
