package com.ssginc8.docto.global.event.appointment;

import java.time.LocalDateTime;

import com.ssginc8.docto.appointment.entity.Appointment;

import lombok.Getter;

/**
 *
 * Appointment가 Request 생성되었을 때
 * 병원관리자가 받는 알림
 */

@Getter
public class AppointmentRequestEvent {

	private final Appointment appointment;

	public AppointmentRequestEvent(Appointment appointment) {
		this.appointment = appointment;
	}
}
