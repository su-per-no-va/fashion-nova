package com.supernova.fashionnova.domain.question;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestion is a Querydsl query type for Question
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestion extends EntityPathBase<Question> {

    private static final long serialVersionUID = 2021029538L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestion question1 = new QQuestion("question1");

    public final com.supernova.fashionnova.global.common.QTimestamped _super = new com.supernova.fashionnova.global.common.QTimestamped(this);

    public final com.supernova.fashionnova.domain.answer.QAnswer answer;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.supernova.fashionnova.domain.order.QOrderDetail orderDetail;

    public final StringPath question = createString("question");

    public final ListPath<QuestionImage, QQuestionImage> questionImageUrls = this.<QuestionImage, QQuestionImage>createList("questionImageUrls", QuestionImage.class, QQuestionImage.class, PathInits.DIRECT2);

    public final EnumPath<QuestionStatus> status = createEnum("status", QuestionStatus.class);

    public final StringPath title = createString("title");

    public final EnumPath<QuestionType> type = createEnum("type", QuestionType.class);

    public final com.supernova.fashionnova.domain.user.QUser user;

    public QQuestion(String variable) {
        this(Question.class, forVariable(variable), INITS);
    }

    public QQuestion(Path<? extends Question> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestion(PathMetadata metadata, PathInits inits) {
        this(Question.class, metadata, inits);
    }

    public QQuestion(Class<? extends Question> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.answer = inits.isInitialized("answer") ? new com.supernova.fashionnova.domain.answer.QAnswer(forProperty("answer"), inits.get("answer")) : null;
        this.orderDetail = inits.isInitialized("orderDetail") ? new com.supernova.fashionnova.domain.order.QOrderDetail(forProperty("orderDetail"), inits.get("orderDetail")) : null;
        this.user = inits.isInitialized("user") ? new com.supernova.fashionnova.domain.user.QUser(forProperty("user")) : null;
    }

}

