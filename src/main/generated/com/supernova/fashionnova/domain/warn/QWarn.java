package com.supernova.fashionnova.domain.warn;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWarn is a Querydsl query type for Warn
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWarn extends EntityPathBase<Warn> {

    private static final long serialVersionUID = 973161122L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWarn warn = new QWarn("warn");

    public final com.supernova.fashionnova.global.common.QTimestamped _super = new com.supernova.fashionnova.global.common.QTimestamped(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath detail = createString("detail");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.supernova.fashionnova.domain.user.QUser user;

    public QWarn(String variable) {
        this(Warn.class, forVariable(variable), INITS);
    }

    public QWarn(Path<? extends Warn> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWarn(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWarn(PathMetadata metadata, PathInits inits) {
        this(Warn.class, metadata, inits);
    }

    public QWarn(Class<? extends Warn> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.supernova.fashionnova.domain.user.QUser(forProperty("user")) : null;
    }

}

