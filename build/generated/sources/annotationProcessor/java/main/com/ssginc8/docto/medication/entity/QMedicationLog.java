package com.ssginc8.docto.medication.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMedicationLog is a Querydsl query type for MedicationLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMedicationLog extends EntityPathBase<MedicationLog> {

    private static final long serialVersionUID = -728731308L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMedicationLog medicationLog = new QMedicationLog("medicationLog");

    public final com.ssginc8.docto.global.base.QBaseTimeEntity _super = new com.ssginc8.docto.global.base.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final QMedicationInformation medication;

    public final QMedicationAlertTime medicationAlertTime;

    public final NumberPath<Long> medicationLogId = createNumber("medicationLogId", Long.class);

    public final EnumPath<MedicationStatus> status = createEnum("status", MedicationStatus.class);

    public final DateTimePath<java.time.LocalDateTime> timeToTake = createDateTime("timeToTake", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMedicationLog(String variable) {
        this(MedicationLog.class, forVariable(variable), INITS);
    }

    public QMedicationLog(Path<? extends MedicationLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMedicationLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMedicationLog(PathMetadata metadata, PathInits inits) {
        this(MedicationLog.class, metadata, inits);
    }

    public QMedicationLog(Class<? extends MedicationLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.medication = inits.isInitialized("medication") ? new QMedicationInformation(forProperty("medication"), inits.get("medication")) : null;
        this.medicationAlertTime = inits.isInitialized("medicationAlertTime") ? new QMedicationAlertTime(forProperty("medicationAlertTime"), inits.get("medicationAlertTime")) : null;
    }

}

