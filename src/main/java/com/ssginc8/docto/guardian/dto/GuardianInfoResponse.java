package com.ssginc8.docto.guardian.dto;

import com.ssginc8.docto.user.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GuardianInfoResponse {
	private Long guardianId;
	private String name;
	private String email;
	private Role role;
}
