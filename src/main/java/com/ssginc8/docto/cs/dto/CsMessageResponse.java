package com.ssginc8.docto.cs.dto;

import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.ssginc8.docto.cs.entity.CsMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CsMessageResponse {
	private Long csMessageId;
	private Long userId;
	private String content;
	private LocalDateTime createdAt;
	private Long csRoomId;

	public static CsMessageResponse from(CsMessage entity) {
		return new CsMessageResponse(
			entity.getCsMessageId(),
			entity.getUserId(),
			entity.getContent(),
			entity.getCreatedAt(),
			entity.getCsRoom().getCsRoomId()
		);
	}

	public static List<CsMessageResponse> fromEntities(List<CsMessage> entities) {
		return entities.stream()
			.map(CsMessageResponse::from)
			.collect(Collectors.toList());
	}
}
