package com.ssginc8.docto.hospital.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProvidedService is a Querydsl query type for ProvidedService
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProvidedService extends EntityPathBase<ProvidedService> {

    private static final long serialVersionUID = 156137958L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProvidedService providedService = new QProvidedService("providedService");

    public final QHospital hospital;

    public final NumberPath<Long> providedServiceId = createNumber("providedServiceId", Long.class);

    public final StringPath serviceName = createString("serviceName");

    public QProvidedService(String variable) {
        this(ProvidedService.class, forVariable(variable), INITS);
    }

    public QProvidedService(Path<? extends ProvidedService> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProvidedService(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProvidedService(PathMetadata metadata, PathInits inits) {
        this(ProvidedService.class, metadata, inits);
    }

    public QProvidedService(Class<? extends ProvidedService> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.hospital = inits.isInitialized("hospital") ? new QHospital(forProperty("hospital"), inits.get("hospital")) : null;
    }

}

