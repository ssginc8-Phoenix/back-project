package com.ssginc8.docto.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = 860747810L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final com.ssginc8.docto.global.base.QBaseTimeEntity _super = new com.ssginc8.docto.global.base.QBaseTimeEntity(this);

    public final com.ssginc8.docto.appointment.entity.QAppointment appointment;

    public final StringPath contents = createString("contents");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final com.ssginc8.docto.doctor.entity.QDoctor doctor;

    public final com.ssginc8.docto.hospital.entity.QHospital hospital;

    public final SetPath<KeywordType, EnumPath<KeywordType>> keywords = this.<KeywordType, EnumPath<KeywordType>>createSet("keywords", KeywordType.class, EnumPath.class, PathInits.DIRECT2);

    public final NumberPath<Long> reportCount = createNumber("reportCount", Long.class);

    public final NumberPath<Long> reviewId = createNumber("reviewId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.ssginc8.docto.user.entity.QUser user;

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.appointment = inits.isInitialized("appointment") ? new com.ssginc8.docto.appointment.entity.QAppointment(forProperty("appointment"), inits.get("appointment")) : null;
        this.doctor = inits.isInitialized("doctor") ? new com.ssginc8.docto.doctor.entity.QDoctor(forProperty("doctor"), inits.get("doctor")) : null;
        this.hospital = inits.isInitialized("hospital") ? new com.ssginc8.docto.hospital.entity.QHospital(forProperty("hospital"), inits.get("hospital")) : null;
        this.user = inits.isInitialized("user") ? new com.ssginc8.docto.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

