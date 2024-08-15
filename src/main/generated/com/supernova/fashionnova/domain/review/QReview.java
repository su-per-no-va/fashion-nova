package com.supernova.fashionnova.domain.review;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = 384771682L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review1 = new QReview("review1");

    public final com.supernova.fashionnova.global.common.QTimestamped _super = new com.supernova.fashionnova.global.common.QTimestamped(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.supernova.fashionnova.domain.product.QProduct product;

    public final NumberPath<Integer> rating = createNumber("rating", Integer.class);

    public final StringPath review = createString("review");

    public final ListPath<ReviewImage, QReviewImage> reviewImageList = this.<ReviewImage, QReviewImage>createList("reviewImageList", ReviewImage.class, QReviewImage.class, PathInits.DIRECT2);

    public final com.supernova.fashionnova.domain.user.QUser user;

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new com.supernova.fashionnova.domain.product.QProduct(forProperty("product")) : null;
        this.user = inits.isInitialized("user") ? new com.supernova.fashionnova.domain.user.QUser(forProperty("user")) : null;
    }

}

