package com.ssginc8.docto.calendar.repo;

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

		return queryFactory
			.select(information.medicationId, information.medicationName, alertTime.timeToTake, alertDay.dayOfWeek)
			.from(information)
			.join(information.alertTimes, alertTime)
			.join(alertTime.alertDays, alertDay)
			.where(information.user.eq(user), information.deletedAt.isNull())
			.fetch();
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

		return query.fetch();
	}

	@Override
	public List<Tuple> fetchMedicationsByGuardian(User guardian) {
		QMedicationInformation qMedicationInformation = QMedicationInformation.medicationInformation;
		QMedicationAlertTime qMedicationAlertTime = QMedicationAlertTime.medicationAlertTime;
		QMedicationAlertDay qMedicationAlertDay = QMedicationAlertDay.medicationAlertDay;
		QPatientGuardian qPatientGuardian = QPatientGuardian.patientGuardian;
		QPatient qPatient = QPatient.patient;

		return queryFactory
			.select(qMedicationInformation.medicationId, qMedicationInformation.medicationName,
				qMedicationAlertTime.timeToTake, qMedicationAlertDay.dayOfWeek, qPatient.user.name)
			.from(qMedicationInformation)
			.join(qMedicationInformation.alertTimes, qMedicationAlertTime)
			.join(qMedicationAlertTime.alertDays, qMedicationAlertDay)
			.join(qPatientGuardian).on(qMedicationInformation.patientGuardianId.eq(qPatientGuardian.patientGuardianId))
			.join(qPatientGuardian.patient, qPatient)
			.where(qPatientGuardian.user.eq(guardian), qMedicationInformation.deletedAt.isNull())
			.fetch();
	}

	@Override
	public List<Tuple> fetchAppointmentsByGuardian(User guardian, CalendarRequest request) {
		QPatient qPatient = QPatient.patient;
		QPatientGuardian qPatientGuardian = QPatientGuardian.patientGuardian;
		QAppointment qAppointment = QAppointment.appointment;
		QHospital qHospital = QHospital.hospital;

		return queryFactory
			.select(qAppointment.appointmentId, qHospital.name, qAppointment.appointmentTime, qPatient.user.name)
			.from(qAppointment)
			.join(qAppointment.hospital, qHospital)
			.join(qAppointment.patientGuardian, qPatientGuardian)
			.join(qPatientGuardian.patient, qPatient)
			.where(qPatientGuardian.user.eq(guardian), qAppointment.deletedAt.isNull(),
				qAppointment.appointmentTime.goe(request.getStartDateTime()),
				qAppointment.appointmentTime.lt(request.getEndDateTime()))
			.orderBy(qAppointment.appointmentTime.asc())
			.fetch();
	}

	@Override
	public List<Tuple> fetchAppointmentsByHospitalAdmin(User hospitalAdmin, CalendarRequest request) {
		QHospital qHospital = QHospital.hospital;
		QDoctor qDoctor = QDoctor.doctor;
		QAppointment qAppointment = QAppointment.appointment;

		return queryFactory
			.select(qAppointment.appointmentId, qHospital.name, qAppointment.appointmentTime, qDoctor.user.name)
			.from(qAppointment)
			.join(qAppointment.doctor, qDoctor)
			.join(qAppointment.hospital, qHospital)
			.where(qAppointment.deletedAt.isNull(), qHospital.user.eq(hospitalAdmin),
				qAppointment.appointmentTime.goe(request.getStartDateTime()),
				qAppointment.appointmentTime.lt(request.getEndDateTime()))
			.orderBy(qAppointment.appointmentTime.asc())
			.fetch();
	}

	@Override
	public List<Tuple> fetchAppointmentsByDoctor(User doctor, CalendarRequest request) {
		QAppointment qAppointment = QAppointment.appointment;
		QHospital qHospital = QHospital.hospital;
		QDoctor qDoctor = QDoctor.doctor;

		return queryFactory
			.select(qAppointment.appointmentId, qHospital.name, qAppointment.appointmentTime)
			.from(qAppointment)
			.join(qAppointment.hospital, qHospital)
			.join(qAppointment.doctor, qDoctor)
			.where(qDoctor.user.eq(doctor))
			.where(qAppointment.deletedAt.isNull(),
				qAppointment.appointmentTime.goe(request.getStartDateTime()),
				qAppointment.appointmentTime.lt(request.getEndDateTime()))
			.orderBy(qAppointment.appointmentTime.asc())
			.fetch();
	}

}
