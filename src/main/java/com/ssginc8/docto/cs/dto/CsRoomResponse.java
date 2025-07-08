package com.ssginc8.docto.cs.dto;

import com.ssginc8.docto.cs.entity.CsRoom;
import com.ssginc8.docto.cs.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CsRoomResponse {

	private final String customerName;
	private final Long   csRoomId;
	private final Long   customerId;
	private final Long   agentId;
	private final String customerAvatarUrl;
	private final Status status;

	// 상담사 정보: null 가능
	private final String agentName;
	private final String agentAvatarUrl;

	/**
	 * 고객 정보만을 갖고 DTO 생성
	 */
	public static CsRoomResponse fromEntity(
		CsRoom  csRoom,
		String  customerName,
		String  customerAvatarUrl,
		Status status
	) {
		return new CsRoomResponse(
			customerName,
			csRoom.getCsRoomId(),
			csRoom.getCustomerId(),
			csRoom.getAgentId(),
			customerAvatarUrl,
			status,
			null,  // agentName
			null   // agentAvatarUrl
		);
	}

	/**
	 * 고객 + 상담사 정보까지 모두 갖고 DTO 생성
	 */
	public static CsRoomResponse fromEntityWithAgent(
		CsRoom  csRoom,
		String  customerName,
		String  customerAvatarUrl,
		String  agentName,
		String  agentAvatarUrl
	) {
		return new CsRoomResponse(
			customerName,
			csRoom.getCsRoomId(),
			csRoom.getCustomerId(),
			csRoom.getAgentId(),
			customerAvatarUrl,
			csRoom.getStatus(),
			agentName,
			agentAvatarUrl
		);
	}
}
