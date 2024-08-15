package com.supernova.fashionnova.domain.mileage;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMileage is a Querydsl query type for Mileage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMileage extends EntityPathBase<Mileage> {

    private static final long serialVersionUID = 156070426L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMileage mileage1 = new QMileage("mileage1");

    public final com.supernova.fashionnova.global.common.QTimestamped _super = new com.supernova.fashionnova.global.common.QTimestamped(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> mileage = createNumber("mileage", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.supernova.fashionnova.domain.user.QUser user;

    public QMileage(String variable) {
        this(Mileage.class, forVariable(variable), INITS);
    }

    public QMileage(Path<? extends Mileage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMileage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMileage(PathMetadata metadata, PathInits inits) {
        this(Mileage.class, metadata, inits);
    }

    public QMileage(Class<? extends Mileage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.supernova.fashionnova.domain.user.QUser(forProperty("user")) : null;
    }

}

