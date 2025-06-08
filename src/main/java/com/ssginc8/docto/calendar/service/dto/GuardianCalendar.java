package com.ssginc8.docto.calendar.service.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

public class GuardianCalendar {
	public static class CalendarItemList {
		private final String name;
		private final List<CalendarItem> calendarItems;

		@Builder
		public CalendarItemList(String name, List<CalendarItem> calendarItems) {
			this.name = name;
			this.calendarItems = calendarItems;
		}
	}

	@Getter
	public static class Response {
		private final List<CalendarItemList> calendarItemLists;

		@Builder
		public Response(List<CalendarItemList> calendarItemLists) {
			this.calendarItemLists = calendarItemLists;
		}
	}
}