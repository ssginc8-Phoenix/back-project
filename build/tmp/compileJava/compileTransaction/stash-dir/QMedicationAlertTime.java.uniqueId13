package com.ssginc8.docto.medication.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMedicationAlertTime is a Querydsl query type for MedicationAlertTime
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMedicationAlertTime extends EntityPathBase<MedicationAlertTime> {

    private static final long serialVersionUID = -319470503L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMedicationAlertTime medicationAlertTime = new QMedicationAlertTime("medicationAlertTime");

    public final com.ssginc8.docto.global.base.QBaseTimeEntity _super = new com.ssginc8.docto.global.base.QBaseTimeEntity(this);

    public final ListPath<MedicationAlertDay, QMedicationAlertDay> alertDays = this.<MedicationAlertDay, QMedicationAlertDay>createList("alertDays", MedicationAlertDay.class, QMedicationAlertDay.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final QMedicationInformation medication;

    public final NumberPath<Long> medicationAlertTimeId = createNumber("medicationAlertTimeId", Long.class);

    public final TimePath<java.time.LocalTime> timeToTake = createTime("timeToTake", java.time.LocalTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMedicationAlertTime(String variable) {
        this(MedicationAlertTime.class, forVariable(variable), INITS);
    }

    public QMedicationAlertTime(Path<? extends MedicationAlertTime> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMedicationAlertTime(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMedicationAlertTime(PathMetadata metadata, PathInits inits) {
        this(MedicationAlertTime.class, metadata, inits);
    }

    public QMedicationAlertTime(Class<? extends MedicationAlertTime> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.medication = inits.isInitialized("medication") ? new QMedicationInformation(forProperty("medication"), inits.get("medication")) : null;
    }

}

