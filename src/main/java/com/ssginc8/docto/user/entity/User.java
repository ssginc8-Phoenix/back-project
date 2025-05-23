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

	@Column(unique = true)
	private String providerId;

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
	private Role role;

	@Column(nullable = false)
	private Boolean isSuspended;

	private LocalDateTime suspendedAt;

	private LocalDateTime suspensionExpiresAt;

	private Long panalty;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "fileId")
	private File profileImage;

	private User(String uuid, String email, String password, String name, String phone,
		LoginType loginType, Role role, File profileImage, Boolean isSuspended) {
		this.uuid = uuid;
		this.email = email;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.loginType = loginType;
		this.role = role;
		this.profileImage = profileImage;
		this.isSuspended = isSuspended;
	}

	public User(String uuid, String email, String providerId, String name, LoginType loginType, Boolean isSuspended) {
		this.uuid = uuid;
		this.email = email;
		this.providerId = providerId;
		this.name = name;
		this.loginType = loginType;
		this.isSuspended = isSuspended;
	}

	public static User createUserByEmail(String email, String password, String name, String phone,
		LoginType loginType, Role role, File profileImage) {
		String uuid = UUID.randomUUID().toString();

		return new User(uuid, email, password, name, phone, loginType, role, profileImage, false);
	}

	public static User createUserBySocial(String providerId, String name, String email, LoginType loginType) {
		String uuid = UUID.randomUUID().toString();

		return new User(uuid, email, providerId, name, loginType, false);
	}

	// 이미 가입 된 사용자가 소셜 로그인으로 로그인 한 경우 호출
	public User updateOauthInfo(String providerId, String name, String email, LoginType loginType) {
		this.providerId = providerId;
		this.name = name;
		this.email = email;
		this.loginType = loginType;

		return this;
	}

	public User updateSocialInfo(String phone, File profileImage, Role role) {
		this.phone = phone;
		this.profileImage = profileImage;
		this.role = role;

		return this;
	}

	public void updateProfileImage(File profileImage) {
		this.profileImage = profileImage;
	}
}
