package com.ssginc8.docto.calendar.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.calendar.provider.CalendarProvider;
import com.ssginc8.docto.calendar.service.dto.CalendarItem;
import com.ssginc8.docto.calendar.service.dto.CalendarRequest;
import com.ssginc8.docto.calendar.service.dto.DoctorCalendar;
import com.ssginc8.docto.calendar.service.dto.GuardianCalendar;
import com.ssginc8.docto.calendar.service.dto.HospitalCalendar;
import com.ssginc8.docto.calendar.service.dto.PatientCalendar;
import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.guardian.provider.PatientGuardianProvider;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CalendarServiceImpl implements CalendarService {
	private final UserService userService;
	private final CalendarProvider calendarProvider;
	private final PatientGuardianProvider patientGuardianProvider;

	@Transactional(readOnly = true)
	@Override
	public PatientCalendar.Response getPatientCalendars(CalendarRequest request) {
		User user = userService.getUserFromUuid();

		List<CalendarItem> calendarItems = CalendarItem.toCalendarItems(
			calendarProvider.getMedicationInformation(user),
			calendarProvider.getAppointmentInformation(user, request),
			request);

		return PatientCalendar.Response.builder()
			.calendarItems(calendarItems)
			.build();
	}

	@Transactional(readOnly = true)
	@Override
	public GuardianCalendar.Response getGuardianCalendars(CalendarRequest request) {
		User user = userService.getUserFromUuid();

		List<GuardianCalendar.CalendarItemList> calendarItemLists = new ArrayList<>();

		List<PatientGuardian> patientGuardians = patientGuardianProvider.getPatientGuardianListByGuardian(user);

		for (PatientGuardian patientGuardian : patientGuardians) {
			List<CalendarItem> calendarItems = CalendarItem.toCalendarItems(
				calendarProvider.getMedicationInformation(patientGuardian.getPatient().getUser()),
				calendarProvider.getAppointmentInformation(patientGuardian.getPatient().getUser(), request),
				request);

			GuardianCalendar.CalendarItemList calendarItemList = GuardianCalendar.CalendarItemList.builder()
				.name(patientGuardian.getPatient().getUser().getName())
				.calendarItems(calendarItems)
				.build();

			calendarItemLists.add(calendarItemList);
		}

		return GuardianCalendar.Response
			.builder()
			.calendarItemLists(calendarItemLists)
			.build();
	}

	@Transactional(readOnly = true)
	@Override
	public HospitalCalendar.Response getHospitalCalendars(CalendarRequest request) {
		// 유저 정보 가져오기

		// 병원 id에 해당하는 의사 리스트 가져오기

		// 리스트 돌면서 현재 의사의 예약 정보 가져오기 -> request 날짜 이용

		// 예약 정보 null이면 다음 의사로 이동

		// List<CalendarItem> 깡통 생성

		// UserCalendar.CalendarItem 채우고 위 깡통에 집어넣기

		// 위 리스트 다 채우면 CalendarItemList 만들기 -> Response에 담기

		return null;
	}

	@Transactional(readOnly = true)
	@Override
	public DoctorCalendar.Response getDoctorCalendars(CalendarRequest request) {
		User user = userService.getUserFromUuid();

		List<CalendarItem> calendarItems = CalendarItem.toAppointmentList(
			calendarProvider.getAppointmentInformation(user, request));

		return DoctorCalendar.Response.builder()
			.calendarItems(calendarItems)
			.build();
	}
}