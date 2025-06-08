package com.ssginc8.docto.calendar.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.calendar.provider.CalendarProvider;
import com.ssginc8.docto.calendar.service.dto.CalendarItem;
import com.ssginc8.docto.calendar.service.dto.CalendarRequest;
import com.ssginc8.docto.calendar.service.dto.DoctorCalendar;
import com.ssginc8.docto.calendar.service.dto.HospitalCalendar;
import com.ssginc8.docto.calendar.service.dto.PatientCalendar;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CalendarServiceImpl implements CalendarService {
	private final UserService userService;
	private final CalendarProvider calendarProvider;

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
		// 유저 정보 가져오기

		// 의사의 예약 정보 가져오기 -> request 날짜 이용

		// 예약 정보 null이면 null 반환

		// List<CalendarItem> 깡통 생성

		// UserCalendar.CalendarItem 채우고 위 깡통에 집어넣기

		// 위 리스트 다 채우면 CalendarItemList 만들기 -> Response에 담기

		return null;
	}
}