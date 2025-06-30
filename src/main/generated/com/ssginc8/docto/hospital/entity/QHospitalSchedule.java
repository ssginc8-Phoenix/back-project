package com.ssginc8.docto.hospital.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHospitalSchedule is a Querydsl query type for HospitalSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHospitalSchedule extends EntityPathBase<HospitalSchedule> {

    private static final long serialVersionUID = -1707149603L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHospitalSchedule hospitalSchedule = new QHospitalSchedule("hospitalSchedule");

    public final DateTimePath<java.time.LocalDateTime> closeTime = createDateTime("closeTime", java.time.LocalDateTime.class);

    public final EnumPath<com.ssginc8.docto.global.base.DayOfWeek> dayOfWeek = createEnum("dayOfWeek", com.ssginc8.docto.global.base.DayOfWeek.class);

    public final QHospital hospital;

    public final NumberPath<Long> hospitalScheduleId = createNumber("hospitalScheduleId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> lunchEnd = createDateTime("lunchEnd", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> lunchStart = createDateTime("lunchStart", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> openTime = createDateTime("openTime", java.time.LocalDateTime.class);

    public QHospitalSchedule(String variable) {
        this(HospitalSchedule.class, forVariable(variable), INITS);
    }

    public QHospitalSchedule(Path<? extends HospitalSchedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHospitalSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHospitalSchedule(PathMetadata metadata, PathInits inits) {
        this(HospitalSchedule.class, metadata, inits);
    }

    public QHospitalSchedule(Class<? extends HospitalSchedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.hospital = inits.isInitialized("hospital") ? new QHospital(forProperty("hospital"), inits.get("hospital")) : null;
    }

}

