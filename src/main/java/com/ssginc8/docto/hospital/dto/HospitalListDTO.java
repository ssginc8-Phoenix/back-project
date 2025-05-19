package com.ssginc8.docto.hospital.dto;

import java.math.BigDecimal;
import java.util.List;

import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.hospital.entity.HospitalSchedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Setter
@Getter
public class HospitalListDTO {

	private Long hospitalId;

	private Long userId;

	private String name;

	private String address;

	private BigDecimal latitude;

	private BigDecimal longitude;

	private String phone;

	private String introduction;

	private String notice;

	private Long waiting;

	private String businessRegistrationNumber;

	private List<HospitalSchedule> schedules;



	public HospitalListDTO(Hospital hospital) {
		this.hospitalId = hospital.getHospitalId();
		this.name = hospital.getName();
	}





	public HospitalListDTO(Long hospitalId, Long userId, String name, String address, String notice, String phone, String introduction, Long waiting, BigDecimal longitude, BigDecimal latitude,
		String businessRegistrationNumber) {
		this.hospitalId = hospitalId;
		this.userId = userId;
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.phone = phone;
		this.introduction = introduction;
		this.notice = notice;
		this.waiting = waiting;
		this.businessRegistrationNumber = businessRegistrationNumber;
	}
}
