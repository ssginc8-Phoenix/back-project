package com.ssginc8.docto.doctor.validator;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.ssginc8.docto.appointment.provider.AppointmentProvider;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.doctor.provider.DoctorScheduleProvider;
import com.ssginc8.docto.global.error.exception.appointmentException.AppointmentDuplicateTimeWindowException;
import com.ssginc8.docto.global.error.exception.appointmentException.DuplicateAppointmentException;
import com.ssginc8.docto.global.error.exception.appointmentException.InvalidAppointmentTimeException;
import com.ssginc8.docto.global.error.exception.doctorException.DoctorOverCapacityException;
import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.patient.entity.Patient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AppointmentValidator {

	private final AppointmentProvider appointmentProvider;
	private final DoctorScheduleProvider doctorScheduleProvider;

	// 예약시간 Validate
	public void validateAppointmentTime(
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

	/*
		동일 환자의 30분(앞, 뒤 15분) 내 중복 예약 검사
	 */
	public void validateDuplicateAppointment(Patient patient, LocalDateTime requestedTime) {
		LocalDateTime startWindow = requestedTime.minusMinutes(15);
		LocalDateTime endWindow = requestedTime.plusMinutes(15);

		boolean hasDuplicate = appointmentProvider.existsByPatientAndTimeRange(patient.getPatientId(), startWindow, endWindow);

		if (hasDuplicate) {
			throw new AppointmentDuplicateTimeWindowException();
		}
	}

	/*
		의사의 30분 내 수용 인원 초과 여부 검사
	 */
	public void validateDoctorSlotCapacity(Doctor doctor, LocalDateTime appointmentTime) {
		Long maxCapacity = doctor.getCapacityPerHalfHour();
		if (maxCapacity == null || maxCapacity <= 0) return;

		LocalDateTime slotStart =
			appointmentTime.withMinute(appointmentTime.getMinute() < 30 ? 0 : 30).withSecond(0).withNano(0);
		LocalDateTime slotEnd = slotStart.plusMinutes(30);

		int currentAppointments =
			appointmentProvider.countAppointmentsInSlot(doctor.getDoctorId(), slotStart, slotEnd);

		if (currentAppointments >= maxCapacity) {
			throw new DoctorOverCapacityException();
		}
	}
}
