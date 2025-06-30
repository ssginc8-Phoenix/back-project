package com.ssginc8.docto.payment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPaymentMethod is a Querydsl query type for PaymentMethod
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentMethod extends EntityPathBase<PaymentMethod> {

    private static final long serialVersionUID = -58397223L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPaymentMethod paymentMethod = new QPaymentMethod("paymentMethod");

    public final com.ssginc8.docto.global.base.QBaseTimeEntity _super = new com.ssginc8.docto.global.base.QBaseTimeEntity(this);

    public final StringPath billingKey = createString("billingKey");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath customerKey = createString("customerKey");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final EnumPath<MethodType> methodType = createEnum("methodType", MethodType.class);

    public final NumberPath<Long> paymentMethodId = createNumber("paymentMethodId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.ssginc8.docto.user.entity.QUser user;

    public QPaymentMethod(String variable) {
        this(PaymentMethod.class, forVariable(variable), INITS);
    }

    public QPaymentMethod(Path<? extends PaymentMethod> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPaymentMethod(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPaymentMethod(PathMetadata metadata, PathInits inits) {
        this(PaymentMethod.class, metadata, inits);
    }

    public QPaymentMethod(Class<? extends PaymentMethod> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.ssginc8.docto.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

