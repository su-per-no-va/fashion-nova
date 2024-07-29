package com.supernova.fashionnova.product;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.supernova.fashionnova.product.dto.ProductResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryQueryImpl implements ProductRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductResponseDto> findProductByOrdered(String sort, String category, String size, String color, Pageable pageable){
        QProduct product = QProduct.product1;
        QProductDetail productDetail = QProductDetail.productDetail;
        OrderSpecifier<?> orderSpecifier;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(product.productStatus.eq(ProductStatus.ACTIVE));

        if(category != null && !category.trim().isEmpty()) {
            ProductCategory productCategory = ProductCategory.valueOf(category.toUpperCase());
            builder.and(product.category.eq(productCategory));
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
                orderSpecifier = product.reviewCount.desc();
                break;
            case "new_item" :
                orderSpecifier = product.createdAt.desc();
                break;
        default:
               orderSpecifier = product.price.desc();
        }
        List<Product> products = queryFactory
            .selectFrom(product)
            .leftJoin(product.productDetailList, productDetail)
            .fetchJoin()
            .where(builder)
            .orderBy(orderSpecifier)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        Long count = queryFactory
            .select(product.count())
            .from(product)
            .leftJoin(product.productDetailList, productDetail)
            .where(builder)
            .fetchOne();
        return new PageImpl<>(products.stream().map(ProductResponseDto::new).collect(Collectors.toList()), pageable, count);
    };
}
