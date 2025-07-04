package com.ssginc8.docto.cs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AssignAgentRequest {

	private Long agentId;
}
