package com.supernova.fashionnova.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryQueryImpl implements ProductRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> findProductByOrdered(String sort, String category, String size, String color, Pageable pageable){
        QProduct product = QProduct.product1;
        QProductDetail productDetail = QProductDetail.productDetail;
        OrderSpecifier<?> orderSpecifier;
        BooleanBuilder builder = new BooleanBuilder();

        if(category != null && !category.trim().isEmpty()) {
            builder.and(product.category.eq(category));
        }
        if(size != null && !size.trim().isEmpty()) {
            builder.and(productDetail.size.eq(size));
        }
        if(color != null && !color.trim().isEmpty()) {
            builder.and(productDetail.color.eq(color));
        }

        switch (sort){
            case "high_price" :
                orderSpecifier = product.price.desc();
                break;
            case "row_price" :
                orderSpecifier = product.price.asc();
                break;
            case "review_count" :
                orderSpecifier = product.review_count.desc();
                break;
            case "new_item" :
                orderSpecifier = product.createdAt.desc();
                break;
        default:
               orderSpecifier = product.price.desc();
        }
        return queryFactory
            .selectFrom(product)
            .leftJoin(productDetail)
            .on(product.id.eq(productDetail.product.id))
            .where(builder)
            .orderBy(orderSpecifier)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    };


}
