package com.ssginc8.docto.calendar.service.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

public class DoctorCalendar {
	@Getter
	public static class Response {
		private final List<CalendarItem> calendarItems;

		@Builder
		public Response(List<CalendarItem> calendarItems) {
			this.calendarItems = calendarItems;
		}
	}
}