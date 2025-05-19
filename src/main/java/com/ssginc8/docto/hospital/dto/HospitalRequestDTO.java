package com.ssginc8.docto.hospital.dto;

import com.ssginc8.docto.hospital.entity.Hospital;
import com.ssginc8.docto.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
//병원 등록/수정
public class HospitalRequestDTO {

	private Long userId;
	private String name;
	private String address;
	private String phone;
	private String introduction;
	private String notice;
	private String businessRegistrationNumber;


	public HospitalRequestDTO(Hospital updatedHospital) {
		this.userId = updatedHospital.getUser().getUserId();
		this.address = updatedHospital.getAddress();
		this.name = updatedHospital.getName();
		this.phone = updatedHospital.getPhone();
		this.introduction = updatedHospital.getIntroduction();
		this.businessRegistrationNumber = updatedHospital.getBusinessRegistrationNumber();
		this.notice = updatedHospital.getNotice();
	}
}
