package com.ssginc8.docto.calendar.service.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.querydsl.core.Tuple;

import lombok.Builder;
import lombok.Getter;

public class HospitalCalendar {
	private static final int DOCTOR_NAME_INDEX = 3;

	@Getter
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
		private Response(List<CalendarItemList> calendarItemLists) {
			this.calendarItemLists = calendarItemLists;
		}
	}

	public static Response toResponse(List<Tuple> tuples) {
		Map<String, List<Tuple>> groupedByDoctor = new HashMap<>();

		for (Tuple tuple : tuples) {
			String doctorName = tuple.get(DOCTOR_NAME_INDEX, String.class);

			groupedByDoctor
				.computeIfAbsent(doctorName, k -> new ArrayList<>())
				.add(tuple);
		}

		return Response.builder()
			.calendarItemLists(groupedByDoctor.entrySet().stream()
				.map(entry -> HospitalCalendar.CalendarItemList.builder()
					.name(entry.getKey())
					.calendarItems(CalendarItem.toAppointmentList(entry.getValue()))
					.build())
				.toList())
			.build();
	}
}