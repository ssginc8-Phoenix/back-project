package com.ssginc8.docto.appointment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentSearchCondition {
	private Long userId;
	private Long hospitalId;
	private Long doctorId;
}
