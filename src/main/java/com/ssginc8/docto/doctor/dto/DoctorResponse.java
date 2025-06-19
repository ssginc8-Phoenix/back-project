package com.ssginc8.docto.doctor.dto;

import com.ssginc8.docto.doctor.entity.Doctor;
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
	private String phone;
	private String email;
	private String imageUrl;
	private Long hospitalId;

	public DoctorResponse(
		Long hospitalId,
		Long doctorId,
		String name,
		String specialization,
		String hospitalName,
		Long capacityPerHalfHour,
		String address,
		String email,
		String phone,
		String imageUrl
	) {
		this.hospitalId = hospitalId;
		this.doctorId = doctorId;
		this.name = name;
		this.specialization = specialization;
		this.hospitalName = hospitalName;
		this.capacityPerHalfHour = capacityPerHalfHour;
		this.address = address;
		this.email = email;
		this.phone = phone;
		this.imageUrl = imageUrl;
	}

	

	public static DoctorResponse from(Doctor doctor, String imageUrl) {

		return new DoctorResponse(
			doctor.getHospital().getHospitalId(),
			doctor.getDoctorId(),
			doctor.getUser().getName(),
			doctor.getSpecialization().getDescription(),
			doctor.getHospital().getName(),
			doctor.getCapacityPerHalfHour(),
	    doctor.getHospital().getAddress(),
			doctor.getUser().getEmail(),
			doctor.getUser().getPhone(),
			imageUrl
		);
	}
}
