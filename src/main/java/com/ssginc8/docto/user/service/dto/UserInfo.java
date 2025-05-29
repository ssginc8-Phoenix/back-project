package com.ssginc8.docto.user.service.dto;

import com.ssginc8.docto.user.entity.Role;
import com.ssginc8.docto.user.entity.User;

public class UserInfo {
	public static class Response {
		public String email;
		public String name;
		public String phone;
		public String address;
		public Role role;
		public String profileImageUrl;

		private Response(String email, String name, String phone, String address, Role role, String profileImageUrl) {
			this.email = email;
			this.name = name;
			this.phone = phone;
			this.address = address;
			this.role = role;
			this.profileImageUrl = profileImageUrl;
		}

		public static Response from(User user) {
			return new Response(user.getEmail(), user.getName(), user.getPhone(), user.getAddress(), user.getRole(),
				user.getProfileUrl());
		}
	}
}
