package com.ssginc8.docto.calendar.repo;

import java.util.List;

import com.querydsl.core.Tuple;
import com.ssginc8.docto.calendar.service.dto.CalendarRequest;
import com.ssginc8.docto.user.entity.User;

public interface QCalendarRepo {
	List<Tuple> getMedicationInformation(User user);

	List<Tuple> getAppointment(User user, CalendarRequest request);

	List<Tuple> fetchAppointmentsByHospitalAdmin(User hospitalAdmin, CalendarRequest request);
}
