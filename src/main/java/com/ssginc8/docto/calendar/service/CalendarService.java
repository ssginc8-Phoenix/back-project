package com.ssginc8.docto.calendar.service;

import com.ssginc8.docto.calendar.service.dto.CalendarRequest;
import com.ssginc8.docto.calendar.service.dto.DoctorCalendar;
import com.ssginc8.docto.calendar.service.dto.HospitalCalendar;
import com.ssginc8.docto.calendar.service.dto.GuardianCalendar;
import com.ssginc8.docto.calendar.service.dto.PatientCalendar;

public interface CalendarService {
	PatientCalendar.Response getPatientCalendars(CalendarRequest request);

	GuardianCalendar.Response getGuardianCalendars(CalendarRequest request);

	HospitalCalendar.Response getHospitalCalendars(CalendarRequest request);

	DoctorCalendar.Response getDoctorCalendars(CalendarRequest request);
}