package com.ssginc8.docto.cs.dto;

import java.time.LocalDateTime;

import com.ssginc8.docto.cs.entity.CsNote;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CsNoteResponse {
	private Long csNoteId;
	private String content;
	private Long agentId;
	private LocalDateTime createdAt;

	public static CsNoteResponse fromEntity(CsNote note) {
		return new CsNoteResponse(
			note.getCsNoteId(),
			note.getContent(),
			note.getAgentId(),
			note.getCreatedAt()
		);
	}
}