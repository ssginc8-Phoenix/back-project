package com.ssginc8.docto.doctor.provider;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Component;

import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.doctor.entity.DoctorSchedule;
import com.ssginc8.docto.doctor.repo.DoctorScheduleRepo;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DoctorScheduleProvider {

	private final DoctorScheduleRepo doctorScheduleRepo;

	public void validateDoctorSchedule(Doctor doctor, LocalDateTime appointmentTime) {
		DayOfWeek day = DayOfWeek.from(appointmentTime);
		DoctorSchedule schedule = doctorScheduleRepo.findByDoctorAndDayOfWeek(doctor, day)
			.orElseThrow(() -> new IllegalArgumentException("해당 요일의 스케쥴이 없습니다."));

		LocalTime time = appointmentTime.toLocalTime();

		if (time.isBefore(schedule.getStartTime()) || time.isAfter(schedule.getEndTime())) {
			throw new IllegalArgumentException("예약 시간이 진료 시간 외입니다.");
		}

		if (!time.isBefore(schedule.getLunchStart()) && !time.isAfter(schedule.getLunchEnd())) {
			throw new IllegalArgumentException("예약 시간이 점심 시간입니다.");
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
}
