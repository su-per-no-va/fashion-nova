package com.supernova.fashionnova.domain.user;

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

    private static final long serialVersionUID = -1234221630L;

    public static final QUser user = new QUser("user");

    public final com.supernova.fashionnova.global.common.QTimestamped _super = new com.supernova.fashionnova.global.common.QTimestamped(this);

    public final ListPath<com.supernova.fashionnova.domain.address.Address, com.supernova.fashionnova.domain.address.QAddress> addressList = this.<com.supernova.fashionnova.domain.address.Address, com.supernova.fashionnova.domain.address.QAddress>createList("addressList", com.supernova.fashionnova.domain.address.Address.class, com.supernova.fashionnova.domain.address.QAddress.class, PathInits.DIRECT2);

    public final ListPath<com.supernova.fashionnova.domain.coupon.Coupon, com.supernova.fashionnova.domain.coupon.QCoupon> couponList = this.<com.supernova.fashionnova.domain.coupon.Coupon, com.supernova.fashionnova.domain.coupon.QCoupon>createList("couponList", com.supernova.fashionnova.domain.coupon.Coupon.class, com.supernova.fashionnova.domain.coupon.QCoupon.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> kakaoId = createNumber("kakaoId", Long.class);

    public final NumberPath<Long> mileage = createNumber("mileage", Long.class);

    public final ListPath<com.supernova.fashionnova.domain.mileage.Mileage, com.supernova.fashionnova.domain.mileage.QMileage> mileageList = this.<com.supernova.fashionnova.domain.mileage.Mileage, com.supernova.fashionnova.domain.mileage.QMileage>createList("mileageList", com.supernova.fashionnova.domain.mileage.Mileage.class, com.supernova.fashionnova.domain.mileage.QMileage.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final StringPath refreshToken = createString("refreshToken");

    public final EnumPath<UserGrade> userGrade = createEnum("userGrade", UserGrade.class);

    public final StringPath userName = createString("userName");

    public final EnumPath<UserRole> userRole = createEnum("userRole", UserRole.class);

    public final EnumPath<UserStatus> userStatus = createEnum("userStatus", UserStatus.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

