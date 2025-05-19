package com.ssginc8.docto.hospital.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.ssginc8.docto.global.base.BaseTimeEntity;
import com.ssginc8.docto.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity


@Table(name = "tbl_hospital")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter

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

	public static Hospital create(User user,
		String name,
		String address,
		BigDecimal latitude,
		BigDecimal longitude,
		String phone,
		String introduction,
		String notice,
		Long waiting,
		String businessRegistrationNumber) {
		Hospital hospital = new Hospital();
		hospital.setUser(user);
		hospital.setName(name);
		hospital.setAddress(address);
		hospital.setLatitude(latitude);
		hospital.setLongitude(longitude);
		hospital.setPhone(phone);
		hospital.setIntroduction(introduction);
		hospital.setNotice(notice);
		hospital.setWaiting(waiting);
		hospital.setBusinessRegistrationNumber(businessRegistrationNumber);
		return hospital;
	}



}
