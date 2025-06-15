package com.ssginc8.docto.hospital.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.ManyToAny;

import com.ssginc8.docto.file.entity.File;
import com.ssginc8.docto.global.base.BaseTimeEntity;
import com.ssginc8.docto.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

	@OneToOne(fetch = FetchType.LAZY)
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
	
	private Long fileId;


	public Hospital(User user, String name, String address, String phone, String introduction, String notice, BigDecimal latitude, BigDecimal longitude, String businessRegistrationNumber, Object o, Long fileId) {
		this.user = user;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.introduction = introduction;
		this.notice = notice;
		this.businessRegistrationNumber = businessRegistrationNumber;
		this.latitude = latitude;
		this.longitude = longitude;
		this.fileId = fileId;
	}

	public static Hospital create(User user,
		String name,
		String address,
		String phone,
		String introduction,
		String businessRegistrationNumber, BigDecimal latitude,
		BigDecimal longitude,
		String notice,
		Long fileId) {
		return new Hospital(user,name,address,phone,introduction,notice,latitude,longitude,businessRegistrationNumber,null,fileId);
	}


	public void updateFromValues(String name, String phone, String address, String introduction, String notice) {
		if (name != null) {
			this.name = name;
		}
		if (phone != null) {
			this.phone = phone;
		}
		if (address != null) {
			this.address = address;
		}
		if (introduction != null) {
			this.introduction = introduction;
		}
		if (notice != null) {
			this.notice = notice;
		}
	}

	public void updateWaiting(Long waiting) {
		this.waiting = waiting;
	}




}

