package com.ssginc8.docto.cs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CsRoomCreateRequest {

	private Long customerId;
}
