package com.ssginc8.docto.cs.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor

public class CsNoteRequest {
	private String content;
	private Long agentId;


}
