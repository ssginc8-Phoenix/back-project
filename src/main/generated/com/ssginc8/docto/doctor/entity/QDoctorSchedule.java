package com.ssginc8.docto.doctor.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDoctorSchedule is a Querydsl query type for DoctorSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDoctorSchedule extends EntityPathBase<DoctorSchedule> {

    private static final long serialVersionUID = -551712217L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDoctorSchedule doctorSchedule = new QDoctorSchedule("doctorSchedule");

    public final com.ssginc8.docto.global.base.QBaseTimeEntity _super = new com.ssginc8.docto.global.base.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<com.ssginc8.docto.global.base.DayOfWeek> dayOfWeek = createEnum("dayOfWeek", com.ssginc8.docto.global.base.DayOfWeek.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final QDoctor doctor;

    public final DateTimePath<java.time.LocalDateTime> endTime = createDateTime("endTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> lunchEnd = createDateTime("lunchEnd", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> lunchStart = createDateTime("lunchStart", java.time.LocalDateTime.class);

    public final NumberPath<Long> scheduleId = createNumber("scheduleId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> startTime = createDateTime("startTime", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDoctorSchedule(String variable) {
        this(DoctorSchedule.class, forVariable(variable), INITS);
    }

    public QDoctorSchedule(Path<? extends DoctorSchedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDoctorSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDoctorSchedule(PathMetadata metadata, PathInits inits) {
        this(DoctorSchedule.class, metadata, inits);
    }

    public QDoctorSchedule(Class<? extends DoctorSchedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.doctor = inits.isInitialized("doctor") ? new QDoctor(forProperty("doctor"), inits.get("doctor")) : null;
    }

}

