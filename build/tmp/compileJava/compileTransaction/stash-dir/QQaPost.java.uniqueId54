package com.ssginc8.docto.qna.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQaPost is a Querydsl query type for QaPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQaPost extends EntityPathBase<QaPost> {

    private static final long serialVersionUID = 767292572L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQaPost qaPost = new QQaPost("qaPost");

    public final com.ssginc8.docto.global.base.QBaseTimeEntity _super = new com.ssginc8.docto.global.base.QBaseTimeEntity(this);

    public final com.ssginc8.docto.appointment.entity.QAppointment appointment;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> qnaPostId = createNumber("qnaPostId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QQaPost(String variable) {
        this(QaPost.class, forVariable(variable), INITS);
    }

    public QQaPost(Path<? extends QaPost> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQaPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQaPost(PathMetadata metadata, PathInits inits) {
        this(QaPost.class, metadata, inits);
    }

    public QQaPost(Class<? extends QaPost> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.appointment = inits.isInitialized("appointment") ? new com.ssginc8.docto.appointment.entity.QAppointment(forProperty("appointment"), inits.get("appointment")) : null;
    }

}

