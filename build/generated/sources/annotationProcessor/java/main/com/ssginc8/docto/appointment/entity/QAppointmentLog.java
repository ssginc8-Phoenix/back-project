package com.ssginc8.docto.appointment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAppointmentLog is a Querydsl query type for AppointmentLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAppointmentLog extends EntityPathBase<AppointmentLog> {

    private static final long serialVersionUID = 1095887212L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAppointmentLog appointmentLog = new QAppointmentLog("appointmentLog");

    public final com.ssginc8.docto.global.base.QBaseTimeEntity _super = new com.ssginc8.docto.global.base.QBaseTimeEntity(this);

    public final QAppointment appointment;

    public final NumberPath<Long> appointmentLogId = createNumber("appointmentLogId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final EnumPath<com.ssginc8.docto.global.base.AppointmentStatus> statusAfter = createEnum("statusAfter", com.ssginc8.docto.global.base.AppointmentStatus.class);

    public final EnumPath<com.ssginc8.docto.global.base.AppointmentStatus> statusBefore = createEnum("statusBefore", com.ssginc8.docto.global.base.AppointmentStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAppointmentLog(String variable) {
        this(AppointmentLog.class, forVariable(variable), INITS);
    }

    public QAppointmentLog(Path<? extends AppointmentLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAppointmentLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAppointmentLog(PathMetadata metadata, PathInits inits) {
        this(AppointmentLog.class, metadata, inits);
    }

    public QAppointmentLog(Class<? extends AppointmentLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.appointment = inits.isInitialized("appointment") ? new QAppointment(forProperty("appointment"), inits.get("appointment")) : null;
    }

}

