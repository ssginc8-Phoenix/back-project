package com.ssginc8.docto.cs.entity;

import com.ssginc8.docto.global.base.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_cs_room")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CsRoom extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long csRoomId;

	@Column(nullable = false)
	private Long customerId;

	private Long agentId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;	// WAITING, OPEN, CLOSED

	public CsRoom(Long customerId, Status status) {
		this.customerId = customerId;
		this.status = status;
	}

	public static CsRoom create(Long customerId) {
		return new CsRoom(customerId, Status.WAITING);
	}

	public void assignAgent(Long agentId) {
		if (this.status != Status.WAITING) {
			throw new IllegalStateException("대기 중인 채팅방에만 상담사를 배정할 수 있습니다.");
		}

		this.agentId = agentId;
		this.status = Status.OPEN;
	}

	public void changeStatus(Status newStatus) {
		if (this.status == Status.CLOSED) {
			throw new IllegalArgumentException("종료된 고객상담은 상태를 변경할 수 없습니다.");
		}
		this.status = newStatus;
	}
}
