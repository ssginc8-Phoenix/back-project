package com.ssginc8.docto.appointment.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssginc8.docto.appointment.dto.AppointmentSearchCondition;
import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.entity.QAppointment;
import com.ssginc8.docto.guardian.entity.QPatientGuardian;
import com.ssginc8.docto.patient.entity.QPatient;
import com.ssginc8.docto.user.entity.QUser;

import jakarta.persistence.EntityManager;

@Repository
public class QAppointmentRepoImpl implements QAppointmentRepo {

	private final JPAQueryFactory queryFactory;

	public QAppointmentRepoImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public Page<Appointment> findAllByCondition(AppointmentSearchCondition condition, Pageable pageable) {
		QAppointment appointment = QAppointment.appointment;
		QPatientGuardian guardian = QPatientGuardian.patientGuardian;
		QPatient patient = QPatient.patient;
		QUser user = QUser.user;

		BooleanBuilder builder = new BooleanBuilder();

		if (condition.getUserId() != null) {
			builder.and(
				guardian.user.userId.eq(condition.getUserId())
					.or(user.userId.eq(condition.getUserId())));
		}

		if (condition.getHospitalId() != null) {
			builder.and(appointment.hospital.hospitalId.eq(condition.getHospitalId()));
		}

		if (condition.getDoctorId() != null) {
			builder.and(appointment.doctor.user.userId.eq(condition.getDoctorId()));
		}

		List<Appointment> content = queryFactory
			.selectFrom(appointment)
			.join(appointment.patientGuardian, guardian).fetchJoin()
			.join(guardian.patient, patient).fetchJoin()
			.join(patient.user, user).fetchJoin()
			.where(builder)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory
			.select(appointment.count())
			.from(appointment)
			.join(appointment.patientGuardian, guardian)
			.join(guardian.patient, patient)
			.join(patient.user, user)
			.where(builder)
			.fetchOne();

		return new PageImpl<>(content, pageable, total);

	}
}
