package com.ssginc8.docto.calendar.service.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.querydsl.core.Tuple;

import lombok.Builder;
import lombok.Getter;

public class GuardianCalendar {
	private static final int APPOINTMENT_PATIENT_NAME_INDEX = 3;
	private static final int MEDICATION_PATIENT_NAME_INDEX = 4;

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
		public Response(List<CalendarItemList> calendarItemLists) {
			this.calendarItemLists = calendarItemLists;
		}
	}

	public static Response toResponse(List<Tuple> appointmentTuples, List<Tuple> medicationTuples, CalendarRequest request) {
		Map<String, List<CalendarItem>> patientCalendarMap = new HashMap<>();

		Map<String, List<Tuple>> appointmentsByPatient = groupTuplesByPatientName(appointmentTuples, APPOINTMENT_PATIENT_NAME_INDEX);
		for (Map.Entry<String, List<Tuple>> entry : appointmentsByPatient.entrySet()) {
			patientCalendarMap.put(entry.getKey(), CalendarItem.toAppointmentList(entry.getValue()));
		}

		Map<String, List<Tuple>> medicationsByPatient = groupTuplesByPatientName(medicationTuples, MEDICATION_PATIENT_NAME_INDEX);
		for (Map.Entry<String, List<Tuple>> entry : medicationsByPatient.entrySet()) {
			String patientName = entry.getKey();
			List<CalendarItem> calendarItems = CalendarItem.toMedicationList(request, entry.getValue());

			if (patientCalendarMap.containsKey(patientName)) {
				patientCalendarMap.get(patientName)
					.addAll(calendarItems);
			} else {
				patientCalendarMap.put(patientName, calendarItems);
			}
		}

		List<CalendarItemList> calendarItemLists = patientCalendarMap.entrySet().stream()
			.map(entry -> CalendarItemList.builder()
				.name(entry.getKey())
				.calendarItems(entry.getValue())
				.build())
			.toList();

		return Response.builder()
			.calendarItemLists(calendarItemLists)
			.build();
	}

	private static Map<String, List<Tuple>> groupTuplesByPatientName(List<Tuple> tuples, int nameIndex) {
		Map<String, List<Tuple>> grouped = new HashMap<>();

		for (Tuple tuple : tuples) {
			String patientName = tuple.get(nameIndex, String.class);
			grouped.computeIfAbsent(patientName, k -> new ArrayList<>()).add(tuple);
		}

		return grouped;
	}

}