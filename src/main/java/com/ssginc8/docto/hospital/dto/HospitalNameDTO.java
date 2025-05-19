package com.ssginc8.docto.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalNameDTO {

	private Long hospitalId;
	private String name;
	private String address;


}
