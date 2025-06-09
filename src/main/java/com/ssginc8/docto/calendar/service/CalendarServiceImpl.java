package com.ssginc8.docto.calendar.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.Tuple;
import com.ssginc8.docto.calendar.provider.CalendarProvider;
import com.ssginc8.docto.calendar.service.dto.CalendarItem;
import com.ssginc8.docto.calendar.service.dto.CalendarRequest;
import com.ssginc8.docto.calendar.service.dto.DoctorCalendar;
import com.ssginc8.docto.calendar.service.dto.GuardianCalendar;
import com.ssginc8.docto.calendar.service.dto.HospitalCalendar;
import com.ssginc8.docto.calendar.service.dto.PatientCalendar;
import com.ssginc8.docto.guardian.provider.PatientGuardianProvider;
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
		User user = getUser();

		return null;

		// List<CalendarItem> calendarItems = CalendarItem.toList(
		// 	calendarProvider.getMedicationInformation(user),
		// 	calendarProvider.getAppointmentInformation(user, request),
		// 	request);
		//
		// return PatientCalendar.Response.builder()
		// 	.calendarItems(calendarItems)
		// 	.build();
	}

	@Transactional(readOnly = true)
	@Override
	public GuardianCalendar.Response getGuardianCalendars(CalendarRequest request) {
		User user = getUser();

		List<Tuple> appointmentTuples = calendarProvider.fetchAppointmentsByGuardian(user, request);
		List<Tuple> medicationTuples = calendarProvider.fetchMedicationsByGuardian(user);

		return GuardianCalendar.toResponse(appointmentTuples, medicationTuples, request);
	}

	@Transactional(readOnly = true)
	@Override
	public HospitalCalendar.Response getHospitalCalendars(CalendarRequest request) {
		User hospitalAdmin = getUser();

		List<Tuple> tuples = calendarProvider.fetchAppointmentsByHospitalAdmin(hospitalAdmin, request);

		return HospitalCalendar.toResponse(tuples);
	}

	@Transactional(readOnly = true)
	@Override
	public DoctorCalendar.Response getDoctorCalendars(CalendarRequest request) {
		User doctor = getUser();

		List<Tuple> tuples = calendarProvider.fetchAppointmentsByDoctor(doctor, request);

		return DoctorCalendar.toResponse(tuples);
	}

	private User getUser() {
		return userService.getUserFromUuid();
	}
}