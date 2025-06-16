package com.ssginc8.docto.doctor.dto;

import org.springframework.web.multipart.MultipartFile;

import com.ssginc8.docto.doctor.entity.Specialization;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class DoctorSaveRequest {

	private Long hospitalId;
	private Specialization specialization;
	private Long userId;


	public DoctorSaveRequest( Long hospitalId, Specialization specialization, Long userId) {

		this.hospitalId = hospitalId;
		this.specialization = specialization;
		this.userId = userId;
	}
}
