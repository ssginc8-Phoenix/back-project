package com.ssginc8.docto.appointment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class Symptom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long symptomId;

	@ManyToOne
	@JoinColumn(name = "appointmentId", nullable = false)
	private Appointment appointment;

	@Column(nullable = false)
	private String name;
}
