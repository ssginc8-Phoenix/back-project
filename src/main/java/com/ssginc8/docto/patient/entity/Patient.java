package com.ssginc8.docto.patient.entity;

import com.ssginc8.docto.global.base.BaseTimeEntity;
import com.ssginc8.docto.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class Patient extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long patientId;

	@OneToOne
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@Column(nullable = false, length = 10)
	String residentRegistrationNumber;
}
