package com.ssginc8.docto.kafka.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KafkaCsMessage {
	private Long csRoomId;
	private Long userId;
	private String content;
	private String messageType;
	private LocalDateTime createdAt;
}
