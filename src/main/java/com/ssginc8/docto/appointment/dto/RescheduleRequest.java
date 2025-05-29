package com.ssginc8.docto.appointment.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RescheduleRequest {
	private LocalDateTime newTime;
}
