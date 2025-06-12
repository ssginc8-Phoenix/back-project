package com.ssginc8.docto.calendar.service.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.querydsl.core.Tuple;
import com.ssginc8.docto.guardian.entity.PatientGuardian;

import lombok.Builder;
import lombok.Getter;

public class GuardianCalendar {
	private static final int APPOINTMENT_PATIENT_NAME_INDEX = 3;
	private static final int MEDICATION_PATIENT_NAME_INDEX = 4;
	private static final int MEDICATION_PATIENT_GUARDIAN_ID_INDEX = 5; // ✅ 추가

	@Getter
	public static class CalendarItemList {
		private final String name;
		private final Long patientGuardianId;
		private final List<CalendarItem> calendarItems;

		@Builder
		public CalendarItemList(String name, Long patientGuardianId, List<CalendarItem> calendarItems) {
			this.name = name;
			this.patientGuardianId = patientGuardianId;
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

	public static Response toResponse(List<Tuple> appointmentTuples, List<Tuple> medicationTuples, List<PatientGuardian> patientGuardians, CalendarRequest request) {
		Map<String, List<CalendarItem>> patientCalendarMap = new HashMap<>();
		Map<String, Long> patientNameToGuardianIdMap = new HashMap<>();

		// 보호자와 연결된 모든 환자 정보 초기화 (약/진료 없어도 포함되도록)
		for (PatientGuardian pg : patientGuardians) {
			String patientName = pg.getPatient().getUser().getName();
			Long pgId = pg.getPatientGuardianId();

			patientCalendarMap.putIfAbsent(patientName, new ArrayList<>());
			patientNameToGuardianIdMap.putIfAbsent(patientName, pgId);
		}

		// 진료 일정 추가
		Map<String, List<Tuple>> appointmentsByPatient = groupTuplesByPatientName(appointmentTuples, APPOINTMENT_PATIENT_NAME_INDEX);
		for (Map.Entry<String, List<Tuple>> entry : appointmentsByPatient.entrySet()) {
			String patientName = entry.getKey();
			List<CalendarItem> items = CalendarItem.fromAppointmentTuples(entry.getValue());
			patientCalendarMap.computeIfAbsent(patientName, k -> new ArrayList<>()).addAll(items);
		}

		// 복약 일정 추가
		Map<String, List<Tuple>> medicationsByPatient = groupTuplesByPatientName(medicationTuples, MEDICATION_PATIENT_NAME_INDEX);
		for (Map.Entry<String, List<Tuple>> entry : medicationsByPatient.entrySet()) {
			String patientName = entry.getKey();
			List<Tuple> tuples = entry.getValue();

			// guardianId가 없어도 CalendarItem은 처리 가능
			if (!patientNameToGuardianIdMap.containsKey(patientName)) {
				Long guardianId = tuples.get(0).get(MEDICATION_PATIENT_GUARDIAN_ID_INDEX, Long.class);
				if (guardianId != null) {
					patientNameToGuardianIdMap.put(patientName, guardianId);
				}
			}

			List<CalendarItem> items = CalendarItem.fromMedicationTuples(tuples, request);
			patientCalendarMap.computeIfAbsent(patientName, k -> new ArrayList<>()).addAll(items);
		}

		// 최종 응답
		List<CalendarItemList> calendarItemLists = patientCalendarMap.entrySet().stream()
			.map(entry -> CalendarItemList.builder()
				.name(entry.getKey())
				.patientGuardianId(patientNameToGuardianIdMap.get(entry.getKey()))
				.calendarItems(entry.getValue())
				.build())
			.toList();

		return Response.builder().calendarItemLists(calendarItemLists).build();
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
