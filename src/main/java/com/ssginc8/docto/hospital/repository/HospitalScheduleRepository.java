package com.ssginc8.docto.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.entity.HospitalSchedule;

public interface HospitalScheduleRepository extends JpaRepository<HospitalSchedule, Long > {


	@Modifying
	@Query("DELETE FROM HospitalSchedule hs WHERE hs.hospital.hospitalId = :hospitalId")
	void deleteByHospitalId(@Param("hospitalId") Long hospitalId);

	List<HospitalSchedule> findByHospital_HospitalId(Long hospitalId);


}
