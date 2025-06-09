package com.ssginc8.docto.calendar.service.dto;

import java.util.List;

import com.querydsl.core.Tuple;

import lombok.Builder;
import lombok.Getter;

public class DoctorCalendar {
	@Getter
	public static class Response {
		private final List<CalendarItem> calendarItems;

		@Builder
		private Response(List<CalendarItem> calendarItems) {
			this.calendarItems = calendarItems;
		}
	}

	public static Response toResponse(List<Tuple> tuples) {
		return Response.builder()
			.calendarItems(CalendarItem.fromAppointmentTuples(tuples))
			.build();
	}
}