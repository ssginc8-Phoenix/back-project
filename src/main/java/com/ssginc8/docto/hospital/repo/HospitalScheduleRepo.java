package com.ssginc8.docto.hospital.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.ssginc8.docto.hospital.entity.HospitalSchedule;

public interface HospitalScheduleRepo extends JpaRepository<HospitalSchedule, Long > {


	void deleteByHospitalHospitalId(Long hospitalId);

	List<HospitalSchedule> findByHospitalHospitalId(Long hospitalId);

	void deleteByHospitalScheduleId(Long hospitalId);
}
