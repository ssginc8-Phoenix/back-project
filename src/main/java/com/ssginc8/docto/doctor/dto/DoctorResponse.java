package com.ssginc8.docto.doctor.dto;

import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.doctor.entity.Specialization;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DoctorResponse {
	private Long doctorId;
	private Specialization specialization;
	private String username;
	private Long hospitalId;
	public DoctorResponse(Long doctorId, Long hospitalId, String username, String specialization) {
		this.doctorId = doctorId;
		this.hospitalId=hospitalId;
		this.username = username;
		this.specialization = Specialization.valueOf(specialization);
	}
	public static DoctorResponse from(Doctor doctor) {
		return new DoctorResponse(
			doctor.getDoctorId(),
			doctor.getHospital().getHospitalId(),
			doctor.getUser().getName(),
			doctor.getSpecialization().name()
		);
	}
}
