package com.ssginc8.docto.hospital.dto;

import com.ssginc8.docto.doctor.entity.Specialization;
import com.ssginc8.docto.hospital.entity.Hospital;

import lombok.*;

@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class HospitalRecommendDTO {
	private Long hospitalId;
	private String name;
	private String address;
	private String phone;
	private int waiting;

	private Specialization specialization;

	public static HospitalRecommendDTO from(Hospital h) {
		return HospitalRecommendDTO.builder()
			.hospitalId(h.getHospitalId())
			.name(h.getName())
			.address(h.getAddress())
			.phone(h.getPhone())
			.waiting(h.getWaiting() == null ? 0 : h.getWaiting().intValue())
			.specialization(h.getSpecialization())
			.build();
	}

}

