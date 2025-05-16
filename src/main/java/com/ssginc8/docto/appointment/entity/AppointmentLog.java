package com.ssginc8.docto.appointment.entity;

import com.ssginc8.docto.global.base.AppointmentStatus;
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
@Table(name = "tbl_appointment_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AppointmentLog extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long appointmentLogId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "appointmentId", nullable = false)
	private Appointment appointment;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AppointmentStatus statusBefore;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AppointmentStatus statusAfter;
}
