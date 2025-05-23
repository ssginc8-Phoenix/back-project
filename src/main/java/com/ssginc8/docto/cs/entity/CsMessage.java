package com.ssginc8.docto.cs.entity;

import com.ssginc8.docto.global.base.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_cs_messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CsMessage extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long csMessageId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "csRoomId", nullable = false)
	private CsRoom csRoom;

	@Column(nullable = false)
	private Long userId;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MessageType messageType;	// TEXT, IMAGE

	public CsMessage(CsRoom csRoom, Long userId, String content, MessageType messageType) {
		this.csRoom = csRoom;
		this.userId = userId;
		this.content = content;
		this.messageType = messageType;
	}

	public static CsMessage create(CsRoom csRoom, Long userId, String content, MessageType messageType) {
		return new CsMessage(csRoom, userId, content, messageType);
	}
}
