package com.ssginc8.docto.hospital.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.ManyToAny;
import com.ssginc8.docto.doctor.entity.Specialization;
import com.ssginc8.docto.file.entity.File;
import com.ssginc8.docto.global.base.BaseTimeEntity;
import com.ssginc8.docto.global.converter.LongListConverter;
import com.ssginc8.docto.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

	@Convert(converter = LongListConverter.class)
	@Column(name = "file_id", columnDefinition = "TEXT")
	private List<Long> fileIds = new ArrayList<>();


	public Hospital(User user, String name, String address, String phone, String introduction, String notice, BigDecimal latitude, BigDecimal longitude, String businessRegistrationNumber, Object o, List<Long> fileIds) {
		this.user = user;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.introduction = introduction;
		this.notice = notice;
		this.businessRegistrationNumber = businessRegistrationNumber;
		this.latitude = latitude;
		this.longitude = longitude;
		this.fileIds = fileIds != null ? fileIds : new ArrayList<>();
	}

	public static Hospital create(User user,
		String name,
		String address,
		String phone,
		String introduction,
		String businessRegistrationNumber, BigDecimal latitude,
		BigDecimal longitude,
		String notice,
		List<Long> fileIds) {
		return new Hospital(user,name,address,phone,introduction,notice,latitude,longitude,businessRegistrationNumber,null,fileIds);
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

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Specialization specialization;



	public void clearFileIds() {
		this.fileIds.clear();
	}

	public void addFileId(Long id) {
		this.fileIds.add(id);
	}

	public void addAllFileIds(Collection<Long> ids) {
		this.fileIds.addAll(ids);
	}



}

