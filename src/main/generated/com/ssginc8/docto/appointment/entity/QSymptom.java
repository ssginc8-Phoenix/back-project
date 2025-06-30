package com.ssginc8.docto.appointment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSymptom is a Querydsl query type for Symptom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSymptom extends EntityPathBase<Symptom> {

    private static final long serialVersionUID = -1157960254L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSymptom symptom = new QSymptom("symptom");

    public final QAppointment appointment;

    public final StringPath name = createString("name");

    public final NumberPath<Long> symptomId = createNumber("symptomId", Long.class);

    public QSymptom(String variable) {
        this(Symptom.class, forVariable(variable), INITS);
    }

    public QSymptom(Path<? extends Symptom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSymptom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSymptom(PathMetadata metadata, PathInits inits) {
        this(Symptom.class, metadata, inits);
    }

    public QSymptom(Class<? extends Symptom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.appointment = inits.isInitialized("appointment") ? new QAppointment(forProperty("appointment"), inits.get("appointment")) : null;
    }

}

