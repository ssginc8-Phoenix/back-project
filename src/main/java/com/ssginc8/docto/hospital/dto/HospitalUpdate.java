package com.ssginc8.docto.hospital.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import lombok.NoArgsConstructor;


@Data

@NoArgsConstructor
@Builder
@AllArgsConstructor

public class HospitalUpdate {


	private String name;
	private String address;
	private String phone;
	private String introduction;
	private String notice;
	private List<String> serviceNames;
	private String businessRegistrationNumber;



}
