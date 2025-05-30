package com.ssginc8.docto.medication.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMedicationInformation is a Querydsl query type for MedicationInformation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMedicationInformation extends EntityPathBase<MedicationInformation> {

    private static final long serialVersionUID = 830354236L;

    public static final QMedicationInformation medicationInformation = new QMedicationInformation("medicationInformation");

    public final com.ssginc8.docto.global.base.QBaseTimeEntity _super = new com.ssginc8.docto.global.base.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> medicationId = createNumber("medicationId", Long.class);

    public final StringPath medicationName = createString("medicationName");

    public final NumberPath<Long> patientGuardianId = createNumber("patientGuardianId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMedicationInformation(String variable) {
        super(MedicationInformation.class, forVariable(variable));
    }

    public QMedicationInformation(Path<? extends MedicationInformation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMedicationInformation(PathMetadata metadata) {
        super(MedicationInformation.class, metadata);
    }

}

