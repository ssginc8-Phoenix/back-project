package com.ssginc8.docto.user.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.DynamicUpdate;

import com.ssginc8.docto.file.entity.File;
import com.ssginc8.docto.global.base.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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

@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "tbl_user")
@Entity
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(nullable = false, unique = true, length = 340)
	private String uuid;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(length = 100)
	private String name;

	@Column(length = 100)
	private String phone;

	@Column(length = 200)
	private String address;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private LoginType loginType;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	private Boolean suspended;

	private LocalDateTime suspendedAt;

	private LocalDateTime suspensionExpiresAt;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "file_id")
	private File profileImage;

	private User(String uuid, String email, String password, String name, String phone, String address,
		LoginType loginType, Role role, File profileImage) {
		this.uuid = uuid;
		this.email = email;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.loginType = loginType;
		this.role = role;
		this.profileImage = profileImage;
	}

	public static User createUser(String email, String password, String name, String phone, String address,
		LoginType loginType, Role role, File profileImage) {
		String uuid = UUID.randomUUID().toString();

		return new User(uuid, email, password, name, phone, address, loginType, role, profileImage);
	}

	public void updateProfileImage(File profileImage) {
		this.profileImage = profileImage;
	}
}
