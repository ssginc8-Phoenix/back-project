package com.ssginc8.docto.medication.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMedicationAlertDay is a Querydsl query type for MedicationAlertDay
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMedicationAlertDay extends EntityPathBase<MedicationAlertDay> {

    private static final long serialVersionUID = -703057776L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMedicationAlertDay medicationAlertDay = new QMedicationAlertDay("medicationAlertDay");

    public final com.ssginc8.docto.global.base.QBaseTimeEntity _super = new com.ssginc8.docto.global.base.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<java.time.DayOfWeek> dayOfWeek = createEnum("dayOfWeek", java.time.DayOfWeek.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> medicationAlertDayId = createNumber("medicationAlertDayId", Long.class);

    public final QMedicationAlertTime medicationAlertTime;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMedicationAlertDay(String variable) {
        this(MedicationAlertDay.class, forVariable(variable), INITS);
    }

    public QMedicationAlertDay(Path<? extends MedicationAlertDay> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMedicationAlertDay(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMedicationAlertDay(PathMetadata metadata, PathInits inits) {
        this(MedicationAlertDay.class, metadata, inits);
    }

    public QMedicationAlertDay(Class<? extends MedicationAlertDay> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.medicationAlertTime = inits.isInitialized("medicationAlertTime") ? new QMedicationAlertTime(forProperty("medicationAlertTime"), inits.get("medicationAlertTime")) : null;
    }

}

