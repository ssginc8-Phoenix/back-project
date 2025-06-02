package com.ssginc8.docto.hospital.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.entity.ProvidedService;

public interface ProvidedServiceRepo extends JpaRepository<ProvidedService, Long > {
	void deleteByHospital(Hospital hospital);
	List<ProvidedService> findByHospitalHospitalId(Long hospitalId);

	void deleteByHospitalHospitalId(Long hospitalId);

	void deleteAllByHospital(Hospital hospital);
}
