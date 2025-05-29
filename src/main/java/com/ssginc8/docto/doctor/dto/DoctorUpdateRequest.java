package com.ssginc8.docto.doctor.dto;

import com.ssginc8.docto.doctor.entity.Specialization;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class DoctorUpdateRequest {
	private String password;
	private String email;
	private Specialization specialization;

	public DoctorUpdateRequest(String password, String email, Specialization specialization) {
		this.password = password;
		this.email = email;
		this.specialization = specialization;
	}
}
