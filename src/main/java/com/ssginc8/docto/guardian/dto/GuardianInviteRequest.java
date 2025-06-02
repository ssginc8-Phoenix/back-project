package com.ssginc8.docto.guardian.dto;

import lombok.Getter;

/**
 * 보호자 초대 요청 DTO
 */
@Getter

public class GuardianInviteRequest {

	private String guardianEmail; // 초대할 보호자 유저 ID
}
