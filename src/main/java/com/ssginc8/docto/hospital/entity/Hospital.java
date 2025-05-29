package com.ssginc8.docto.hospital.entity;

import java.math.BigDecimal;

import com.ssginc8.docto.global.base.BaseTimeEntity;
import com.ssginc8.docto.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
}
