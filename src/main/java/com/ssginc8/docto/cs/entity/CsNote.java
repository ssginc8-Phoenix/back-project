package com.ssginc8.docto.cs.entity;

import jakarta.persistence.Id;

import com.ssginc8.docto.global.base.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_cs_notes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CsNote extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long csNoteId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "csRoomId", nullable = false)
	private CsRoom csRoom;

	@Column(nullable = false)
	private Long agentId; // 상담사 ID

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content; // 상담 내용 요약 또는 메모

	public CsNote(CsRoom csRoom, Long agentId, String content) {
		this.csRoom = csRoom;
		this.agentId = agentId;
		this.content = content;
	}

	public static CsNote create(CsRoom csRoom, Long agentId, String content) {
		return new CsNote(csRoom, agentId, content);
	}
}

