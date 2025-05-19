package com.ssginc8.docto.hospital.provider;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.repo.HospitalRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HospitalProvider {

	private final HospitalRepo hospitalRepo;

	@Transactional(readOnly = true)
	public Hospital getHospitalById(Long hospitalId) {
		return hospitalRepo.findById(hospitalId).orElseThrow(
			() -> new IllegalArgumentException("해당 병원이 존재하지 않습니다. id = " + hospitalId)
		);
	}
}
