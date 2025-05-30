package com.ssginc8.docto.doctor.dto;

import com.ssginc8.docto.doctor.entity.Specialization;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class DoctorSaveRequest {

	private Long hospitalId;
	private Specialization specialization;

	private String username;
	private String password;
	private String email;
	private String login_type;
	private String role;
	private boolean suspended;
	private String uuid;

	public DoctorSaveRequest( Long hospitalId, Specialization specialization, String username, String password, String email, String login_type, String role, boolean suspended, String uuid) {

		this.hospitalId = hospitalId;
		this.specialization = specialization;
		this.username = username;
		this.password = password;
		this.email = email;
		this.login_type = login_type;
		this.role = role;
		this.suspended = suspended;
		this.uuid = uuid;
	}
}
