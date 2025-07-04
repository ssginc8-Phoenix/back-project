package com.ssginc8.docto.user.service.dto;

import com.ssginc8.docto.user.entity.Role;
import com.ssginc8.docto.user.entity.User;

import lombok.Getter;

public class AdminUserList {
	@Getter
	public static class Response {
		private final String email;
		private final String name;
		private final Role role;
		private final String profileImageUrl;

		private Response(String email, String name, Role role, String profileImageUrl) {
			this.email = email;
			this.name = name;
			this.role = role;
			this.profileImageUrl = profileImageUrl;
		}

		public static Response from(User user) {
			return new Response(user.getEmail(), user.getName(), user.getRole(), user.getProfileUrl());
		}
	}
}
