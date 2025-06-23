package com.ssginc8.docto.chatbot.dto;

import com.ssginc8.docto.hospital.entity.Hospital;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HospitalSummary {
	private Long id;
	private String name;
	private String address;
	private String phone;


	public static HospitalSummary from(Hospital h) {
		return new HospitalSummary(
			h.getHospitalId(),
			h.getName(),
			h.getAddress(),
			h.getPhone()
		);
	}
}
