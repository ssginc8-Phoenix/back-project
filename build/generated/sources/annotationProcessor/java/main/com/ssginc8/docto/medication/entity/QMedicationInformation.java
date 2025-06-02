package com.ssginc8.docto.medication.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMedicationInformation is a Querydsl query type for MedicationInformation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMedicationInformation extends EntityPathBase<MedicationInformation> {

    private static final long serialVersionUID = 830354236L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMedicationInformation medicationInformation = new QMedicationInformation("medicationInformation");

    public final com.ssginc8.docto.global.base.QBaseTimeEntity _super = new com.ssginc8.docto.global.base.QBaseTimeEntity(this);

    public final ListPath<MedicationAlertTime, QMedicationAlertTime> alertTimes = this.<MedicationAlertTime, QMedicationAlertTime>createList("alertTimes", MedicationAlertTime.class, QMedicationAlertTime.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> medicationId = createNumber("medicationId", Long.class);

    public final StringPath medicationName = createString("medicationName");

    public final NumberPath<Long> patientGuardianId = createNumber("patientGuardianId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.ssginc8.docto.user.entity.QUser user;

    public QMedicationInformation(String variable) {
        this(MedicationInformation.class, forVariable(variable), INITS);
    }

    public QMedicationInformation(Path<? extends MedicationInformation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMedicationInformation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMedicationInformation(PathMetadata metadata, PathInits inits) {
        this(MedicationInformation.class, metadata, inits);
    }

    public QMedicationInformation(Class<? extends MedicationInformation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.ssginc8.docto.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

