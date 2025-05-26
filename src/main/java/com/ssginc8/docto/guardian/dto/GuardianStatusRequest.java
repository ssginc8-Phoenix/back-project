package com.ssginc8.docto.guardian.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 보호자 권한 상태 변경 요청 DTO
 * status 값으로 "ACCEPTED" 또는 "REJECTED" 문자열을 받음
 * - inviteCode: 엔티티에 저장된 초대 코드 (User.uuid)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuardianStatusRequest {
	private String status;
	private String inviteCode;
}
