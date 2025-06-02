package com.ssginc8.docto.doctor.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.user.entity.User;

public interface DoctorRepo extends JpaRepository<Doctor, Long> {

	List<Doctor> findByHospitalHospitalId(Long hospitalId);

	List<Doctor> findByHospital(Hospital hospital);
	void deleteAll(Iterable<? extends Doctor> doctors);
	Page<Doctor> findByDeletedAtIsNull(Pageable pageable);
	boolean existsByUser(User user);
}
