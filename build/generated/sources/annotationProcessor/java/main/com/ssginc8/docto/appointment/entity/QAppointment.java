package com.ssginc8.docto.appointment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAppointment is a Querydsl query type for Appointment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAppointment extends EntityPathBase<Appointment> {

    private static final long serialVersionUID = -1012324680L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAppointment appointment = new QAppointment("appointment");

    public final com.ssginc8.docto.global.base.QBaseTimeEntity _super = new com.ssginc8.docto.global.base.QBaseTimeEntity(this);

    public final NumberPath<Long> appointmentId = createNumber("appointmentId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> appointmentTime = createDateTime("appointmentTime", java.time.LocalDateTime.class);

    public final EnumPath<AppointmentType> appointmentType = createEnum("appointmentType", AppointmentType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final com.ssginc8.docto.doctor.entity.QDoctor doctor;

    public final com.ssginc8.docto.hospital.entity.QHospital hospital;

    public final com.ssginc8.docto.guardian.entity.QPatientGuardian patientGuardian;

    public final EnumPath<PaymentType> paymentType = createEnum("paymentType", PaymentType.class);

    public final NumberPath<Long> queueNumber = createNumber("queueNumber", Long.class);

    public final EnumPath<AppointmentStatus> status = createEnum("status", AppointmentStatus.class);

    public final StringPath symptom = createString("symptom");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAppointment(String variable) {
        this(Appointment.class, forVariable(variable), INITS);
    }

    public QAppointment(Path<? extends Appointment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAppointment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAppointment(PathMetadata metadata, PathInits inits) {
        this(Appointment.class, metadata, inits);
    }

    public QAppointment(Class<? extends Appointment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.doctor = inits.isInitialized("doctor") ? new com.ssginc8.docto.doctor.entity.QDoctor(forProperty("doctor"), inits.get("doctor")) : null;
        this.hospital = inits.isInitialized("hospital") ? new com.ssginc8.docto.hospital.entity.QHospital(forProperty("hospital"), inits.get("hospital")) : null;
        this.patientGuardian = inits.isInitialized("patientGuardian") ? new com.ssginc8.docto.guardian.entity.QPatientGuardian(forProperty("patientGuardian"), inits.get("patientGuardian")) : null;
    }

}

