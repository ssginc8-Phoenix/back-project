package com.ssginc8.docto.calendar.service.dto;

import java.util.List;

import com.querydsl.core.Tuple;

import lombok.Builder;
import lombok.Getter;

public class PatientCalendar {
	@Getter
	public static class Response {
		private final List<CalendarItem> calendarItems;

		@Builder
		private Response(List<CalendarItem> calendarItems) {
			this.calendarItems = calendarItems;
		}
	}

	public static Response toResponse(List<Tuple> appointmentTuples, List<Tuple> medicationTuples, CalendarRequest request) {
		return Response.builder()
			.calendarItems(CalendarItem.mergeAndSort(medicationTuples, appointmentTuples, request))
			.build();
	}

}