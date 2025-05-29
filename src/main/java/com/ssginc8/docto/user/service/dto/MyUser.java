package com.ssginc8.docto.user.service.dto;

import java.time.LocalDateTime;

import com.ssginc8.docto.user.entity.Role;
import com.ssginc8.docto.user.entity.User;

import lombok.Getter;

@Getter
public class MyUser {
	private Long userId;
	private String uuid;
	private String email;
	private String name;
	private String phone;
	private String address;
	private Role role;
	private Boolean isSuspended;
	private LocalDateTime suspendedAt;
	private LocalDateTime suspensionExpiresAt;
	private Long panalty;
	private String profileImageUrl;

	private MyUser(Long userId, String uuid, String email, String name, String phone, String address, Role role,
		Boolean isSuspended, LocalDateTime suspendedAt, LocalDateTime suspensionExpiresAt, Long panalty,
		String profileImageUrl) {
		this.userId = userId;
		this.uuid = uuid;
		this.email = email;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.role = role;
		this.isSuspended = isSuspended;
		this.suspendedAt = suspendedAt;
		this.suspensionExpiresAt = suspensionExpiresAt;
		this.panalty = panalty;
		this.profileImageUrl = profileImageUrl;
	}

	public static MyUser from(User user) {
		return new MyUser(user.getUserId(), user.getUuid(), user.getEmail(), user.getName(), user.getPhone(),
			user.getAddress(), user.getRole(), user.getIsSuspended(), user.getSuspendedAt(),
			user.getSuspensionExpiresAt(),
			user.getPenalty(), user.getProfileImage().getUrl());
	}
}
