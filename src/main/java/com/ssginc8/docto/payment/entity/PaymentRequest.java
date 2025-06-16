package com.ssginc8.docto.payment.entity;

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
@Table(name = "tbl_payment_request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PaymentRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentRequestId;

	@Column(nullable = false)
	private Long appointmentId;

	@Column(nullable = false)
	private Long amount;

	@Enumerated(EnumType.STRING)
	private RequestStatus status;

	private PaymentRequest(Long appointmentId, Long amount, RequestStatus status) {
		this.appointmentId = appointmentId;
		this.amount = amount;
		this.status = status;
	}

	public static PaymentRequest create(Long appointmentId, Long amount) {
		return new PaymentRequest(appointmentId, amount, RequestStatus.REQUESTED);
	}
}
