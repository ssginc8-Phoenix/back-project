package com.ssginc8.docto.auth.jwt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String uuid;

	@Column(nullable = false, length = 1000)
	private String refreshToken;

	private RefreshToken(String uuid, String refreshToken) {
		this.uuid = uuid;
		this.refreshToken = refreshToken;
	}

	public static RefreshToken createRefreshToken(String uuid, String refreshToken) {
		return new RefreshToken(uuid, refreshToken);
	}

	public RefreshToken update(String newRefreshToken) {
		this.refreshToken = newRefreshToken;
		return this;
	}
}
