package com.ssginc8.docto.doctor.provider;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.doctor.repo.DoctorRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DoctorProvider {

	private final DoctorRepo doctorRepo;

	@Transactional(readOnly = true)
	public Doctor getDoctorById(Long doctorId) {
		return doctorRepo.findById(doctorId).orElseThrow(
			() -> new IllegalArgumentException("해당 의사가 존재하지 않습니다. id = " + doctorId)
		);
	}
}
