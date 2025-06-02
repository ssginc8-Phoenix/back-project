package com.ssginc8.docto.doctor.provider;

import static com.ssginc8.docto.global.error.ErrorCode.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ssginc8.docto.doctor.dto.DoctorScheduleRequest;
import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.doctor.entity.DoctorSchedule;
import com.ssginc8.docto.doctor.repo.DoctorScheduleRepo;
import com.ssginc8.docto.global.error.exception.doctorException.DoctorLunchTimeConflictException;
import com.ssginc8.docto.global.error.exception.doctorException.DoctorScheduleNotFoundException;
import com.ssginc8.docto.global.error.exception.doctorException.HospitalScheduleNotFoundException;
import com.ssginc8.docto.global.error.exception.doctorException.InvalidDoctorScheduleLunchException;
import com.ssginc8.docto.global.error.exception.doctorException.InvalidDoctorScheduleOutOfHoursException;
import com.ssginc8.docto.global.error.exception.doctorException.InvalidDoctorScheduleTimeException;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.entity.HospitalSchedule;
import com.ssginc8.docto.hospital.provider.HospitalProvider;
import com.ssginc8.docto.hospital.repo.HospitalScheduleRepo;
import com.ssginc8.docto.global.error.exception.appointmentException.AppointmentInLunchTimeException;
import com.ssginc8.docto.global.error.exception.appointmentException.AppointmentOutOfWorkingHoursException;
import com.ssginc8.docto.global.error.exception.doctorScheduleException.DoctorScheduleNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DoctorScheduleProvider {

	private final DoctorScheduleRepo doctorScheduleRepo;
	private final HospitalProvider hospitalProvider;


	public void validateDoctorSchedule(Doctor doctor, LocalDateTime appointmentTime) {
		DayOfWeek day = DayOfWeek.from(appointmentTime);
		DoctorSchedule schedule = doctorScheduleRepo.findByDoctorAndDayOfWeek(doctor, day)
			.orElseThrow(DoctorScheduleNotFoundException::new);

		LocalTime time = appointmentTime.toLocalTime();

		if (time.isBefore(schedule.getStartTime()) || time.isAfter(schedule.getEndTime())) {
			throw new InvalidDoctorScheduleOutOfHoursException();
		}

		if (!time.isBefore(schedule.getLunchStart()) && !time.isAfter(schedule.getLunchEnd())) {
			throw new InvalidDoctorScheduleLunchException();
		}
	}

	public DoctorSchedule saveDoctorSchedule(DoctorSchedule doctorSchedule) {
		return doctorScheduleRepo.save(doctorSchedule);
	}

	public DoctorSchedule getDoctorScheduleById(Long scheduleId) {
		return doctorScheduleRepo.findById(scheduleId)
			.orElseThrow(DoctorScheduleNotFoundException::new);
	}

	public void validateWithinHospitalSchedule(Doctor doctor, DoctorScheduleRequest scheduleRequest) {
		Hospital hospital = doctor.getHospital();
		HospitalSchedule hospitalSchedule = hospitalProvider.getScheduleByDay(hospital.getHospitalId(), scheduleRequest.getDayOfWeek());

		if (hospitalSchedule == null) {
			throw new HospitalScheduleNotFoundException();
		}

		if (scheduleRequest.getStartTime().isBefore(hospitalSchedule.getOpenTime()) ||
			scheduleRequest.getEndTime().isAfter(hospitalSchedule.getCloseTime())) {
			throw new InvalidDoctorScheduleTimeException();
		}

		if (scheduleRequest.getLunchStart() != null && scheduleRequest.getLunchEnd() != null) {
			if (scheduleRequest.getLunchStart().isBefore(hospitalSchedule.getLunchStart()) ||
				scheduleRequest.getLunchEnd().isAfter(hospitalSchedule.getLunchEnd())) {
				throw new DoctorLunchTimeConflictException();
			}
		}
	}

	public List<DoctorSchedule> getSchedulesByDoctorId(Long doctorId) {
		return doctorScheduleRepo.findAllByDoctorDoctorId(doctorId);
	}

	public void deleteDoctorSchedule(DoctorSchedule schedule) {
		doctorScheduleRepo.delete(schedule);
	}

	public void deleteByDoctorId(Long doctorId) {
		List<DoctorSchedule> schedules = doctorScheduleRepo.findAllByDoctorDoctorId(doctorId);
		doctorScheduleRepo.deleteAll(schedules);
	}

	public DoctorSchedule getScheduleByDoctorAndDay(Doctor doctor, DayOfWeek dayOfWeek) {
		return doctorScheduleRepo.findByDoctorAndDayOfWeek(doctor, dayOfWeek)
			.orElseThrow();
	}
}
