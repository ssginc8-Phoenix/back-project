package com.ssginc8.docto.cs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CsMessageRequest {

	private Long csRoomId;
	private Long userId;
	private String content;
	private String messageType;
}
