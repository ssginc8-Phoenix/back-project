package com.ssginc8.docto.appointment.provider;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.repo.AppointmentRepo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppointmentProvider {

	private final AppointmentRepo appointmentRepo;

	@Transactional(readOnly = true)
	public Appointment getAppointmentById(Long appointmentId) {
		return appointmentRepo.findById(appointmentId).orElseThrow(
			() -> new IllegalArgumentException("해당 예약이 존재하지 않습니다. id = " + appointmentId));
	}
}
