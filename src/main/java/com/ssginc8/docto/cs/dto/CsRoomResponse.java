package com.ssginc8.docto.cs.dto;

import java.time.LocalDateTime;

import com.ssginc8.docto.cs.entity.CsRoom;
import com.ssginc8.docto.cs.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CsRoomResponse {

	private final String customerName;
	private final Long csRoomId;
	private final Long customerId;
	private final Long agentId;
	private final String customerAvatarUrl;
	private final Status status;


	/**
	 * CsRoom 엔티티와 유저·최신 메시지 정보를 받아서 DTO 생성
	 *
	 * @param csRoom               CsRoom 엔티티
	 * @param customerName         고객 이름
	 * @param customerAvatarUrl    고객 프로필 이미지 URL
	 * @return CsRoomResponse DTO
	 */
	public static CsRoomResponse fromEntity(
		CsRoom csRoom,
		String customerName,
		String customerAvatarUrl
	) {
		return new CsRoomResponse(
			customerName,
			csRoom.getCsRoomId(),
			csRoom.getCustomerId(),
			csRoom.getAgentId(),
			customerAvatarUrl,
			csRoom.getStatus()

		);
	}
}
