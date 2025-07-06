package com.ssginc8.docto.cs.dto;

import com.ssginc8.docto.cs.entity.CsRoom;
import com.ssginc8.docto.cs.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CsRoomResponse {

	private Long csRoomId;
	private Long customerId;
	private Long agentId;
	private Status status;

	public static CsRoomResponse fromEntity(CsRoom csRoom) {
		return new CsRoomResponse(
			csRoom.getCsRoomId(),
			csRoom.getCustomerId(),
			csRoom.getAgentId(),
			csRoom.getStatus()
		);
	}
}
