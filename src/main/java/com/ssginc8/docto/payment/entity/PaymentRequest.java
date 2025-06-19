package com.ssginc8.docto.payment.entity;

import org.hibernate.annotations.DynamicUpdate;

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
@Table(name = "tbl_payment_request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Getter
public class PaymentRequest extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentRequestId;

	private String orderId;

	@Column(nullable = false)
	private Long appointmentId;

	@Column(nullable = false)
	private Long amount;

	private String customerKey;

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

	public void updatePaymentInfo(String orderId, String customerKey) {
		this.orderId = orderId;
		this.customerKey = customerKey;
	}

	public void updateStatus() {
		this.status = RequestStatus.COMPLETED;
	}
}
