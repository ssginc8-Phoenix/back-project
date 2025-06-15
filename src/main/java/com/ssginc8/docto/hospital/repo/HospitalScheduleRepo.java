package com.ssginc8.docto.hospital.repo;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.ssginc8.docto.hospital.entity.HospitalSchedule;

public interface HospitalScheduleRepo extends JpaRepository<HospitalSchedule, Long > {


	Optional<HospitalSchedule> findByHospitalHospitalIdAndDayOfWeek(Long hospitalId, DayOfWeek dayOfWeek);


	List<HospitalSchedule> findByHospitalHospitalId(Long hospitalId);

	List<HospitalSchedule> findByHospitalHospitalIdIn(List<Long> hospitalIds);

	void deleteAllByHospitalHospitalId(Long hospitalId);
}
