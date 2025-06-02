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
	private String hospitalName;
	public DoctorResponse(Long doctorId, String hospitalName, String username, String specialization) {
		this.doctorId = doctorId;
		this.hospitalName=hospitalName;
		this.username = username;
		this.specialization = Specialization.valueOf(specialization);
	}
	public static DoctorResponse from(Doctor doctor) {
		return new DoctorResponse(
			doctor.getDoctorId(),
			doctor.getHospital().getName(),
			doctor.getUser().getName(),
			doctor.getSpecialization().name()
		);
	}
}
