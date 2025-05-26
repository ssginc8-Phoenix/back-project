package com.ssginc8.docto.user.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicUpdate;

import com.ssginc8.docto.global.base.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DynamicUpdate
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

	@Column(nullable = false)
	private Boolean suspended;

	private LocalDateTime suspendedAt;

	private LocalDateTime suspensionExpiresAt;

	public static User createUser(String username, String password, String email, String loginType, String role, Boolean suspended, String uuid) {
		User user = new User();
		user.name = username;
		user.password = password;
		user.email = email;
		user.loginType = LoginType.valueOf(loginType);
		user.role = Role.valueOf(role);
		user.suspended = suspended;
		user.uuid = uuid;
		// 필요한 초기화 작업 추가
		return user;
	}


	// User.java
	public void updatePassword(String password) {
		this.password = password;
	}

	public void updateEmail(String email) {
		this.email = email;
	}
}
