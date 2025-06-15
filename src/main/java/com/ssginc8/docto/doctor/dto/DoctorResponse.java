package com.ssginc8.docto.doctor.dto;

import com.ssginc8.docto.doctor.entity.Doctor;
import com.ssginc8.docto.doctor.entity.Specialization;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DoctorResponse {
	private Long doctorId;
	private String specialization;
	private String name;
	private String hospitalName;
	private Long capacityPerHalfHour;
	private String address;






	public DoctorResponse(Long doctorId, String name, String specialization, String name1, Long capacityPerHalfHour, String address) {
		
    this.doctorId = doctorId;
		this.name = name;
		this.specialization = specialization;
		this.hospitalName = name1;
		this.capacityPerHalfHour = capacityPerHalfHour;
		this.address = address;
	}


	public static DoctorResponse from(Doctor doctor) {
		return new DoctorResponse(
			doctor.getDoctorId(),
			doctor.getUser().getName(),
			doctor.getSpecialization().name(),
			doctor.getHospital().getName(),
			doctor.getCapacityPerHalfHour(),
			doctor.getHospital().getAddress()
		);
	}

}
