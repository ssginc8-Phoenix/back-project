package com.ssginc8.docto.hospital.entity;

import java.math.BigDecimal;


import org.hibernate.annotations.DynamicUpdate;

import com.ssginc8.docto.global.base.BaseTimeEntity;
import com.ssginc8.docto.hospital.dto.HospitalUpdate;
import com.ssginc8.docto.user.entity.User;


import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity


@Table(name = "tbl_hospital")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DynamicUpdate

public class Hospital extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long hospitalId;

	@OneToOne
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, length = 200)
	private String address;

	@Column(precision = 9, scale = 6)
	private BigDecimal latitude;

	@Column(precision = 9, scale = 6)
	private BigDecimal longitude;

	@Column(nullable = false, length = 100)
	private String phone;

	@Column(columnDefinition = "TEXT")
	private String introduction;

	@Column(columnDefinition = "TEXT")
	private String notice;

	private Long waiting;

	@Column(nullable = false, length = 200)
	private String businessRegistrationNumber;



	public Hospital(User user, String name, String address, String phone, String introduction, String notice, String businessRegistrationNumber, BigDecimal latitude, BigDecimal longitude, Long waiting) {
		this.user = user;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.introduction = introduction;
		this.notice = notice;
		this.businessRegistrationNumber = businessRegistrationNumber;
		this.latitude = latitude;
		this.longitude = longitude;
		this.waiting = waiting;

	}



	public static Hospital create(User user,
		String name,
		String address,
		String phone,
		String introduction,
		BigDecimal latitude,
		BigDecimal longitude,
		String notice,
		String businessRegistrationNumber) {
		return new Hospital(user,name,address,phone,introduction,notice,businessRegistrationNumber,latitude,longitude,null);
	}

	public void updateFromDTO(HospitalUpdate dto) {
		if (dto.getName() != null) this.name = dto.getName();
		if (dto.getAddress() != null) this.address = dto.getAddress();
		if (dto.getPhone() != null) this.phone = dto.getPhone();
		if (dto.getIntroduction() != null) this.introduction = dto.getIntroduction();
		if (dto.getBusinessRegistrationNumber() != null) this.businessRegistrationNumber = dto.getBusinessRegistrationNumber();
		if (dto.getNotice() != null) this.notice = dto.getNotice();


	}

	public void updateWaiting(Long waiting) {
		this.waiting = waiting;
	}

}

