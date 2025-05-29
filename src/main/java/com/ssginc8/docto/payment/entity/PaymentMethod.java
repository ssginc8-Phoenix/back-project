package com.ssginc8.docto.payment.entity;

import com.ssginc8.docto.global.base.BaseTimeEntity;
import com.ssginc8.docto.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "tbl_payment_method")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PaymentMethod extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentMethodId;

	@ManyToOne
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@Column(nullable = false)
	private String billingKey;

	@Column(nullable = false)
	private String customerKey;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MethodType methodType;
}
