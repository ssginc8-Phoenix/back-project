package com.ssginc8.docto.hospital.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
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

	private String businessRegistrationNumber;

	private Long waiting;

	private List<String> serviceNames;

	private List<HospitalScheduleResponse> schedules;

	private String imageUrl; // ✅ 이미지 URL 포함

	public HospitalResponse(Hospital hospital) {
		this.hospitalId = hospital.getHospitalId();
		this.address = hospital.getAddress();
		this.name = hospital.getName();
		this.latitude = hospital.getLatitude();
		this.longitude = hospital.getLongitude();
		this.phone = hospital.getPhone();
		this.notice = hospital.getNotice();
		this.introduction = hospital.getIntroduction();
		this.serviceNames = new ArrayList<>();
		this.schedules = new ArrayList<>();
		this.businessRegistrationNumber = hospital.getBusinessRegistrationNumber();
	}
	public static HospitalResponse from(Hospital hospital, String imageUrl, List<String> serviceNames) {
		HospitalResponse res = new HospitalResponse();
		res.hospitalId = hospital.getHospitalId();
		res.name = hospital.getName();
		res.address = hospital.getAddress();
		res.latitude = hospital.getLatitude();
		res.longitude = hospital.getLongitude();
		res.phone = hospital.getPhone();
		res.introduction = hospital.getIntroduction();
		res.notice = hospital.getNotice();
		res.waiting = hospital.getWaiting();
		res.serviceNames = serviceNames != null ? serviceNames : new ArrayList<>();
		res.businessRegistrationNumber = hospital.getBusinessRegistrationNumber();
		res.imageUrl = imageUrl;
		return res;
	}

}
