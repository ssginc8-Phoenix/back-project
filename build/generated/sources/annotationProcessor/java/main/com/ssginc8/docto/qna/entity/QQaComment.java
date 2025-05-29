package com.ssginc8.docto.qna.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQaComment is a Querydsl query type for QaComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQaComment extends EntityPathBase<QaComment> {

    private static final long serialVersionUID = 1938768099L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQaComment qaComment = new QQaComment("qaComment");

    public final com.ssginc8.docto.global.base.QBaseTimeEntity _super = new com.ssginc8.docto.global.base.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> qnaCommentId = createNumber("qnaCommentId", Long.class);

    public final QQaPost qnaPostId;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QQaComment(String variable) {
        this(QaComment.class, forVariable(variable), INITS);
    }

    public QQaComment(Path<? extends QaComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQaComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQaComment(PathMetadata metadata, PathInits inits) {
        this(QaComment.class, metadata, inits);
    }

    public QQaComment(Class<? extends QaComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.qnaPostId = inits.isInitialized("qnaPostId") ? new QQaPost(forProperty("qnaPostId"), inits.get("qnaPostId")) : null;
    }

}

