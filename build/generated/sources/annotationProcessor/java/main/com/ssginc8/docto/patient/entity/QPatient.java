package com.ssginc8.docto.patient.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPatient is a Querydsl query type for Patient
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPatient extends EntityPathBase<Patient> {

    private static final long serialVersionUID = 243971320L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPatient patient = new QPatient("patient");

    public final com.ssginc8.docto.global.base.QBaseTimeEntity _super = new com.ssginc8.docto.global.base.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> patientId = createNumber("patientId", Long.class);

    public final StringPath residentRegistrationNumber = createString("residentRegistrationNumber");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.ssginc8.docto.user.entity.QUser user;

    public QPatient(String variable) {
        this(Patient.class, forVariable(variable), INITS);
    }

    public QPatient(Path<? extends Patient> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPatient(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPatient(PathMetadata metadata, PathInits inits) {
        this(Patient.class, metadata, inits);
    }

    public QPatient(Class<? extends Patient> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.ssginc8.docto.user.entity.QUser(forProperty("user")) : null;
    }

}

