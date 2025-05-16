package com.ssginc8.docto.hospital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "tbl_provided_service")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProvidedService {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long providedServiceId;

	@ManyToOne
	@JoinColumn(name = "hospitalId", nullable = false)
	private Hospital hospital;

	@Column(nullable = false)
	private String serviceName;
}
