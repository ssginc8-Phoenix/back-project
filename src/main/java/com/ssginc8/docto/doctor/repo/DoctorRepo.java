package com.ssginc8.docto.doctor.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.doctor.entity.Doctor;

public interface DoctorRepo extends JpaRepository<Doctor, Long> {
}
