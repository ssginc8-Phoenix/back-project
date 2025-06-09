package com.ssginc8.docto.appointment.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.cglib.core.Local;

import groovy.util.logging.Log4j;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Log4j2
public class RescheduleRequest {
	private String newTime;

	public LocalDateTime getNewTimeAsLocalDateTime() {
		return LocalDateTime.parse(newTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}
}
