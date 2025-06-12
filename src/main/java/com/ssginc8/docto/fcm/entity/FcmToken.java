package com.ssginc8.docto.fcm.entity;

import com.ssginc8.docto.global.base.BaseTimeEntity;
import com.ssginc8.docto.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

/**
 * User <-> FcmToken: ManyToOne 관계 (User 1명은 여러 Token 보유 가능)
 */

@Entity
@Table(name = "tbl_fcm_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FcmToken extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fcmId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@Column(nullable = false, unique = true, length = 512)
	private String token;

	public FcmToken(User user, String token) {
		this.user = user;
		this.token = token;
	}

	public void updateToken(User user, String newToken) {
		this.user = user;
		this.token = newToken;
	}
}
