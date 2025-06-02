package com.ssginc8.docto.hospital.dto;

import java.math.BigDecimal;
import java.util.List;

import com.ssginc8.docto.hospital.entity.Hospital;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data

@NoArgsConstructor
@Builder
@Getter
@AllArgsConstructor
public class HospitalResponse {

	private Long hospitalId;

	private String name;

	private String address;

	private BigDecimal latitude;

	private BigDecimal longitude;

	private String phone;

	private String introduction;

	private String notice;

	private Long waiting;

	private List<String> serviceNames;

	public HospitalResponse(Long hospitalId, String name) {
		 this.hospitalId = hospitalId;
		 this.name = name;
	}

	public HospitalResponse(Hospital hospital) {
		this.hospitalId = hospital.getHospitalId();
		this.name = hospital.getName();
		this.latitude = hospital.getLatitude();
		this.longitude = hospital.getLongitude();
	}
}
