package com.ssginc8.docto.cs.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.ssginc8.docto.cs.entity.CsMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class CsMessageResponse {
	private Long csMessageId;
	private Long userId;
	private String content;
	private LocalDateTime createdAt;
	private Long csRoomId;
	private boolean system;

	// 기존 static factory 도 수정
	public static CsMessageResponse from(CsMessage entity) {
		return CsMessageResponse.builder()
			.csMessageId(entity.getCsMessageId())
			.userId(entity.getUserId())
			.content(entity.getContent())
			.createdAt(entity.getCreatedAt())
			.csRoomId(entity.getCsRoom().getCsRoomId())
			.system(false)  // 기본 false
			.build();
	}

	public static List<CsMessageResponse> fromEntities(List<CsMessage> list) {
		return list.stream()
			.map(CsMessageResponse::from)
			.collect(Collectors.toList());
	}
}