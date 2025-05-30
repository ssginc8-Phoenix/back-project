package com.ssginc8.docto.doctor.provider;

import static com.ssginc8.docto.global.error.ErrorCode.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Component;

import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.doctor.entity.DoctorSchedule;
import com.ssginc8.docto.doctor.repo.DoctorScheduleRepo;
import com.ssginc8.docto.global.error.exception.appointmentException.AppointmentInLunchTimeException;
import com.ssginc8.docto.global.error.exception.appointmentException.AppointmentOutOfWorkingHoursException;
import com.ssginc8.docto.global.error.exception.doctorScheduleException.DoctorScheduleNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DoctorScheduleProvider {

	private final DoctorScheduleRepo doctorScheduleRepo;

	public void validateDoctorSchedule(Doctor doctor, LocalDateTime appointmentTime) {
		DayOfWeek day = DayOfWeek.from(appointmentTime);
		DoctorSchedule schedule = doctorScheduleRepo.findByDoctorAndDayOfWeek(doctor, day)
			.orElseThrow(DoctorScheduleNotFoundException::new);

		LocalTime time = appointmentTime.toLocalTime();

		if (time.isBefore(schedule.getStartTime()) || time.isAfter(schedule.getEndTime())) {
			throw new AppointmentOutOfWorkingHoursException();
		}

		if (!time.isBefore(schedule.getLunchStart()) && !time.isAfter(schedule.getLunchEnd())) {
			throw new AppointmentInLunchTimeException();
		}
	}

	public DoctorSchedule saveDoctorSchedule(DoctorSchedule doctorSchedule) {
		doctorScheduleRepo.save(doctorSchedule);
		return doctorSchedule;
	}

	public DoctorSchedule getDoctorScheduleById(Long scheduleId) {
		return doctorScheduleRepo.findById(scheduleId)
			.orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + scheduleId));
	}

	public DoctorSchedule getScheduleByDoctorAndDay(Doctor doctor, DayOfWeek dayOfWeek) {
		return doctorScheduleRepo.findByDoctorAndDayOfWeek(doctor, dayOfWeek)
			.orElseThrow();
	}
}
