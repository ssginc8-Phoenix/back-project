package com.ssginc8.docto.doctor.repo;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.doctor.entity.DoctorSchedule;

public interface DoctorScheduleRepo extends JpaRepository<DoctorSchedule, Long> {

	Optional<DoctorSchedule> findByDoctorAndDayOfWeek(Doctor doctor, DayOfWeek day);

	List<DoctorSchedule> findAllByDoctorDoctorId(Long doctorId);

	void deleteByDoctor(Doctor doctor);
}
