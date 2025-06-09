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

	public static CalendarItem createMedicationItem(LocalDate date, LocalTime time, String title, Long relatedId) {
		return new CalendarItem(date, time, title, relatedId, ItemType.MEDICATION);
	}

	public static CalendarItem createAppointmentItem(LocalDate date, LocalTime time, String title, Long relatedId) {
		return new CalendarItem(date, time, title, relatedId, ItemType.APPOINTMENT);
	}

	public static List<CalendarItem> toList(List<Tuple> medicationTuples, List<Tuple> appointmentTuples, CalendarRequest request) {
		List<CalendarItem> calendarItems = toMedicationList(request, medicationTuples);
		calendarItems.addAll(toAppointmentList(appointmentTuples));

		calendarItems.sort(Comparator
			.comparing(CalendarItem::getDate)
			.thenComparing(CalendarItem::getTime));

		return calendarItems;
	}

	public static List<CalendarItem> toMedicationList(CalendarRequest request, List<Tuple> tuples) {
		List<CalendarItem> calendarItems = new ArrayList<>();

		int year = request.getYear();
		int month = request.getMonth();
		YearMonth yearMonth = YearMonth.of(year, month);

		for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
			LocalDate currentDate = LocalDate.of(year, month, day);
			DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();

			for (Tuple tuple : tuples) {
				Long medicationId = tuple.get(0, Long.class);
				String medicationName = tuple.get(1, String.class);
				LocalTime time = tuple.get(2, LocalTime.class);
				DayOfWeek scheduledDay = tuple.get(3, DayOfWeek.class);

				if (currentDayOfWeek.equals(scheduledDay)) {
					calendarItems.add(
						CalendarItem.createMedicationItem(currentDate, time, medicationName, medicationId));
				}
			}
		}

		return calendarItems;
	}

	public static List<CalendarItem> toAppointmentList(List<Tuple> tuples) {
		List<CalendarItem> calendarItems = new ArrayList<>();

		for (Tuple tuple : tuples) {
			Long appointmentId = tuple.get(0, Long.class);
			String hospitalName = tuple.get(1, String.class);
			LocalDateTime localDateTime = tuple.get(2, LocalDateTime.class);

			calendarItems.add(
				CalendarItem.createAppointmentItem(localDateTime.toLocalDate(), localDateTime.toLocalTime(),
					hospitalName, appointmentId));
		}

		return calendarItems;
	}
}
