package com.ssginc8.docto.calendar.provider;

import java.util.List;

import org.springframework.stereotype.Service;

import com.querydsl.core.Tuple;
import com.ssginc8.docto.calendar.repo.QCalendarRepo;
import com.ssginc8.docto.calendar.service.dto.CalendarRequest;
import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.user.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CalendarProvider {
	private final QCalendarRepo qCalendarRepo;

	public List<Tuple> fetchMedicationsByPatient(User patient) {
		return qCalendarRepo.fetchMedicationsByPatient(patient);
	}

	public List<Tuple> fetchAppointmentsByPatient(User patient, CalendarRequest request) {
		return qCalendarRepo.fetchAppointmentsByPatient(patient, request);
	}

	public List<Tuple> fetchMedicationsByGuardian(User guardian) {
		return qCalendarRepo.fetchMedicationsByGuardian(guardian);
	}

	public List<Tuple> fetchAppointmentsByGuardian(User guardian, CalendarRequest request) {
		return qCalendarRepo.fetchAppointmentsByGuardian(guardian, request);
	}

	public List<Tuple> fetchAppointmentsByHospitalAdmin(User hospitalAdmin, CalendarRequest request) {
		return qCalendarRepo.fetchAppointmentsByHospitalAdmin(hospitalAdmin, request);
	}

	public List<Tuple> fetchAppointmentsByDoctor(User doctor, CalendarRequest request) {
		return qCalendarRepo.fetchAppointmentsByDoctor(doctor, request);
	}

	public List<PatientGuardian> fetchAcceptedGuardiansByGuardianUser(User guardianUser) {
		return qCalendarRepo.fetchAcceptedGuardiansByGuardianUser(guardianUser);
	}

	public List<Tuple> fetchMedicationsByGuardian(User guardian) {
		return qCalendarRepo.fetchMedicationsByGuardian(guardian);
	}

	public List<Tuple> fetchAppointmentsByGuardian(User guardian, CalendarRequest request) {
		return qCalendarRepo.fetchAppointmentsByGuardian(guardian, request);
	}

	public List<Tuple> fetchAppointmentsByHospitalAdmin(User hospitalAdmin, CalendarRequest request) {
		return qCalendarRepo.fetchAppointmentsByHospitalAdmin(hospitalAdmin, request);
	}

	public List<Tuple> fetchAppointmentsByDoctor(User doctor, CalendarRequest request) {
		return qCalendarRepo.fetchAppointmentsByDoctor(doctor, request);
	}
}
