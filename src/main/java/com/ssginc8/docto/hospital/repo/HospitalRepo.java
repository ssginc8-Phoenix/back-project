package com.ssginc8.docto.hospital.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssginc8.docto.hospital.entity.Hospital;

public interface HospitalRepo extends JpaRepository<Hospital, Long> {
}
