package com.ssginc8.docto.global.event.appointment;

import com.ssginc8.docto.appointment.entity.Appointment;

import lombok.Getter;

@Getter
public class AppointmentStatusChangedEvent {

	private final Appointment appointment;
	private final boolean isPenaltyCancellation;

	public AppointmentStatusChangedEvent(Appointment appointment, boolean isPenaltyCancellation) {
		this.appointment = appointment;
		this.isPenaltyCancellation = isPenaltyCancellation;
	}
}
