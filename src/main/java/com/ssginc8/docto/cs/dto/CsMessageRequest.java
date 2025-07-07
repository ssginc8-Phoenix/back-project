package com.ssginc8.docto.cs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CsMessageRequest {

	private Long csRoomId;
	private Long userId;
	private String content;
	private boolean system;
}
