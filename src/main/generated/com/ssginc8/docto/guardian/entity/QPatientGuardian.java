package com.ssginc8.docto.guardian.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPatientGuardian is a Querydsl query type for PatientGuardian
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPatientGuardian extends EntityPathBase<PatientGuardian> {

    private static final long serialVersionUID = 518036755L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPatientGuardian patientGuardian = new QPatientGuardian("patientGuardian");

    public final DateTimePath<java.time.LocalDateTime> invitedAt = createDateTime("invitedAt", java.time.LocalDateTime.class);

    public final com.ssginc8.docto.patient.entity.QPatient patient;

    public final NumberPath<Long> patientGuardianId = createNumber("patientGuardianId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> respondedAt = createDateTime("respondedAt", java.time.LocalDateTime.class);

    public final EnumPath<Status> status = createEnum("status", Status.class);

    public final com.ssginc8.docto.user.entity.QUser user;

    public QPatientGuardian(String variable) {
        this(PatientGuardian.class, forVariable(variable), INITS);
    }

    public QPatientGuardian(Path<? extends PatientGuardian> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPatientGuardian(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPatientGuardian(PathMetadata metadata, PathInits inits) {
        this(PatientGuardian.class, metadata, inits);
    }

    public QPatientGuardian(Class<? extends PatientGuardian> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.patient = inits.isInitialized("patient") ? new com.ssginc8.docto.patient.entity.QPatient(forProperty("patient"), inits.get("patient")) : null;
        this.user = inits.isInitialized("user") ? new com.ssginc8.docto.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

