package com.ssginc8.docto.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1994991544L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final com.ssginc8.docto.global.base.QBaseTimeEntity _super = new com.ssginc8.docto.global.base.QBaseTimeEntity(this);

    public final StringPath address = createString("address");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath email = createString("email");

    public final BooleanPath isSuspended = createBoolean("isSuspended");

    public final EnumPath<LoginType> loginType = createEnum("loginType", LoginType.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final NumberPath<Long> penalty = createNumber("penalty", Long.class);

    public final StringPath phone = createString("phone");

    public final com.ssginc8.docto.file.entity.QFile profileImage;

    public final StringPath providerId = createString("providerId");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final DateTimePath<java.time.LocalDateTime> suspendedAt = createDateTime("suspendedAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> suspensionExpiresAt = createDateTime("suspensionExpiresAt", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final StringPath uuid = createString("uuid");

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.profileImage = inits.isInitialized("profileImage") ? new com.ssginc8.docto.file.entity.QFile(forProperty("profileImage")) : null;
    }

}

