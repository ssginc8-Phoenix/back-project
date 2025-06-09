package com.ssginc8.docto.calendar.service.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.querydsl.core.Tuple;

import lombok.Getter;

@Getter
public class CalendarItem {
	private final LocalDate date;
	private final LocalTime time;
	private final String title;
	private final Long relatedId;
	private final ItemType itemType;

	private CalendarItem(LocalDate date, LocalTime time, String title, Long relatedId, ItemType itemType) {
		this.date = date;
		this.time = time;
		this.title = title;
		this.relatedId = relatedId;
		this.itemType = itemType;
	}

	public static List<CalendarItem> mergeAndSort(List<Tuple> medicationTuples, List<Tuple> appointmentTuples, CalendarRequest request) {
		List<CalendarItem> calendarItems = fromMedicationTuples(medicationTuples, request);
		calendarItems.addAll(fromAppointmentTuples(appointmentTuples));

		calendarItems.sort(Comparator
			.comparing(CalendarItem::getDate)
			.thenComparing(CalendarItem::getTime));

		return calendarItems;
	}

	public static List<CalendarItem> fromMedicationTuples(List<Tuple> tuples, CalendarRequest request) {
		List<CalendarItem> items = new ArrayList<>();
		YearMonth yearMonth = YearMonth.of(request.getYear(), request.getMonth());

		for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
			LocalDate date = LocalDate.of(request.getYear(), request.getMonth(), day);
			DayOfWeek dayOfWeek = date.getDayOfWeek();

			for (Tuple tuple : tuples) {
				if (dayOfWeek.equals(tuple.get(3, DayOfWeek.class))) {
					items.add(fromMedicationTuple(tuple, date));
				}
			}
		}

		return items;
	}

	private static CalendarItem fromMedicationTuple(Tuple tuple, LocalDate date) {
		Long id = tuple.get(0, Long.class);
		String title = tuple.get(1, String.class);
		LocalTime time = tuple.get(2, LocalTime.class);

		return new CalendarItem(date, time, title, id, ItemType.MEDICATION);
	}

	public static List<CalendarItem> fromAppointmentTuples(List<Tuple> tuples) {
		List<CalendarItem> items = new ArrayList<>();
		for (Tuple tuple : tuples) {
			items.add(fromAppointmentTuple(tuple));
		}
		return items;
	}

	private static CalendarItem fromAppointmentTuple(Tuple tuple) {
		Long id = tuple.get(0, Long.class);
		String title = tuple.get(1, String.class);
		LocalDateTime dateTime = tuple.get(2, LocalDateTime.class);

		return new CalendarItem(dateTime.toLocalDate(), dateTime.toLocalTime(), title, id, ItemType.APPOINTMENT);
	}
}
