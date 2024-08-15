package com.supernova.fashionnova.domain.question;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestionImage is a Querydsl query type for QuestionImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestionImage extends EntityPathBase<QuestionImage> {

    private static final long serialVersionUID = -708044839L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestionImage questionImage = new QQuestionImage("questionImage");

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QQuestion question;

    public final StringPath questionImageUrl = createString("questionImageUrl");

    public QQuestionImage(String variable) {
        this(QuestionImage.class, forVariable(variable), INITS);
    }

    public QQuestionImage(Path<? extends QuestionImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestionImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestionImage(PathMetadata metadata, PathInits inits) {
        this(QuestionImage.class, metadata, inits);
    }

    public QQuestionImage(Class<? extends QuestionImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.question = inits.isInitialized("question") ? new QQuestion(forProperty("question"), inits.get("question")) : null;
    }

}

