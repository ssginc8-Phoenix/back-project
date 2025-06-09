package com.ssginc8.docto.calendar.repo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssginc8.docto.appointment.entity.QAppointment;
import com.ssginc8.docto.calendar.service.dto.CalendarRequest;
import com.ssginc8.docto.doctor.entity.QDoctor;
import com.ssginc8.docto.guardian.entity.QPatientGuardian;
import com.ssginc8.docto.hospital.entity.QHospital;
import com.ssginc8.docto.medication.entity.QMedicationAlertDay;
import com.ssginc8.docto.medication.entity.QMedicationAlertTime;
import com.ssginc8.docto.medication.entity.QMedicationInformation;
import com.ssginc8.docto.patient.entity.QPatient;
import com.ssginc8.docto.user.entity.Role;
import com.ssginc8.docto.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Repository
public class QCalendarRepoImpl implements QCalendarRepo {
	private final JPAQueryFactory queryFactory;

	@Override
	@Transactional(readOnly = true)
	public List<Tuple> getMedicationInformation(User user) {
		QMedicationInformation information = QMedicationInformation.medicationInformation;
		QMedicationAlertTime alertTime = QMedicationAlertTime.medicationAlertTime;
		QMedicationAlertDay alertDay = QMedicationAlertDay.medicationAlertDay;

		List<Tuple> tuples = queryFactory
			.select(information.medicationId, information.medicationName, alertTime.timeToTake, alertDay.dayOfWeek)
			.from(information)
			.join(information.alertTimes, alertTime)
			.join(alertTime.alertDays, alertDay)
			.where(information.user.eq(user), information.deletedAt.isNull())
			.fetch();

		log.info(tuples);

		return tuples;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Tuple> getAppointment(User user, CalendarRequest request) {
		QPatient patient = QPatient.patient;
		QPatientGuardian patientGuardian = QPatientGuardian.patientGuardian;
		QAppointment appointment = QAppointment.appointment;
		QHospital hospital = QHospital.hospital;
		QDoctor doctor = QDoctor.doctor;

		JPAQuery<Tuple> query = queryFactory
			.select(appointment.appointmentId, hospital.name, appointment.appointmentTime)
			.from(appointment)
			.join(appointment.hospital, hospital);

		if (Objects.equals(user.getRole(), Role.PATIENT)) {
			query
				.join(appointment.patientGuardian, patientGuardian)
				.join(patientGuardian.patient, patient)
				.where(patient.user.eq(user));
		} else if (Objects.equals(user.getRole(), Role.DOCTOR)) {
			query
				.join(appointment.doctor, doctor)
				.where(doctor.user.eq(user));
		}

		query.where(appointment.deletedAt.isNull(),
			appointment.appointmentTime.goe(request.getStartDateTime()),
			appointment.appointmentTime.lt(request.getEndDateTime()));

		List<Tuple> tuples = query.fetch();

		log.info(tuples.toString());

		return tuples;
	}

	@Override
	public List<Tuple> fetchAppointmentsByHospitalAdmin(User hospitalAdmin, CalendarRequest request) {
		QHospital hospital = QHospital.hospital;
		QDoctor doctor = QDoctor.doctor;
		QAppointment appointment = QAppointment.appointment;

		return queryFactory
			.select(appointment.appointmentId, hospital.name, appointment.appointmentTime, doctor.user.name)
			.from(appointment)
			.join(appointment.doctor, doctor)
			.join(appointment.hospital, hospital)
			.where(appointment.deletedAt.isNull(), hospital.user.eq(hospitalAdmin),
				appointment.appointmentTime.goe(request.getStartDateTime()),
				appointment.appointmentTime.lt(request.getEndDateTime()))
			.orderBy(appointment.appointmentTime.asc())
			.fetch();
	}

}
