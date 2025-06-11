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
	private Long capacityPerHalfHour;


	public DoctorResponse(Long doctorId, String username, Specialization specialization, String hospitalName, Long capacityPerHalfHour) {
		this.doctorId = doctorId;
		this.username = username;
		this.specialization = specialization;
		this.hospitalName = hospitalName;
		this.capacityPerHalfHour = capacityPerHalfHour;
	}

	public static DoctorResponse from(Doctor doctor) {
		return new DoctorResponse(
			doctor.getDoctorId(),
			doctor.getUser().getName(),
			doctor.getSpecialization(),
			doctor.getHospital().getName(),
			doctor.getCapacityPerHalfHour()
		);
	}

}
