package com.ssginc8.docto.calendar.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssginc8.docto.calendar.service.CalendarService;
import com.ssginc8.docto.calendar.service.dto.CalendarRequest;
import com.ssginc8.docto.calendar.service.dto.DoctorCalendar;
import com.ssginc8.docto.calendar.service.dto.GuardianCalendar;
import com.ssginc8.docto.calendar.service.dto.PatientCalendar;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/calendar")
@RestController
public class CalendarApiController {
	private final CalendarService calendarService;

	@GetMapping("/patient")
	public PatientCalendar.Response getPatientCalendar(@ModelAttribute CalendarRequest request) {
		return calendarService.getPatientCalendars(request);
	}

	@GetMapping("/guardian")
	public GuardianCalendar.Response getGuardianCalendar(@ModelAttribute CalendarRequest request) {
		return null;
	}

	@GetMapping("/doctor")
	public DoctorCalendar.Response getDoctorCalendar(@ModelAttribute CalendarRequest request) {
		return calendarService.getDoctorCalendars(request);
	}

	@GetMapping("/hospital")
	public GuardianCalendar.Response getHospitalCalendar(@ModelAttribute CalendarRequest request) {
		return null;
	}
}