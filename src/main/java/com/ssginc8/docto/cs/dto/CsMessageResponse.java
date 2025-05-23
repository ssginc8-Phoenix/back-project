package com.ssginc8.docto.cs.dto;

import java.time.LocalDateTime;

import com.ssginc8.docto.cs.entity.CsMessage;
import com.ssginc8.docto.cs.entity.MessageType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CsMessageResponse {
	private Long csMessageId;
	private Long userId;
	private String content;
	private MessageType messageType;
	private LocalDateTime createdAt;

	public static CsMessageResponse from(CsMessage entity) {
		return new CsMessageResponse(
			entity.getCsMessageId(),
			entity.getUserId(),
			entity.getContent(),
			entity.getMessageType(),
			entity.getCreatedAt()
		);
	}
}
