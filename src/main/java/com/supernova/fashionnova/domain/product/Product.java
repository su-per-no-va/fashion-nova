package com.supernova.fashionnova.domain.product;

import com.supernova.fashionnova.domain.review.Review;
import com.supernova.fashionnova.global.common.Timestamped;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String product;

    @Column(nullable = false)
    private Long price;

    private String explanation;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @Column(nullable = false)
    private int wishCount;

    @Column(nullable = false)
    private int reviewCount;
    @Column
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDetail> productDetailList = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Review> reviews;

    @Builder
    public Product(String product, Long price, String explanation, ProductCategory category, ProductStatus productStatus) {
        this.product = product;
        this.price = price;
        this.explanation = explanation;
        this.category = category;
        this.productStatus = productStatus;
        this.wishCount = 0;
        this.reviewCount = 0;
    }

    public void updateProduct(String product, Long price, String explanation, ProductCategory category, ProductStatus productStatus) {
        this.product = product;
        this.price = price;
        this.explanation = explanation;
        this.category = category;
        this.productStatus = productStatus;
    }

    public void addDetail(ProductDetail detail) {

        ProductDetail productDetail = productDetailList.stream()
            .filter(p -> p.getColor().equals(detail.getColor()) && p.getSize().equals(detail.getSize())).findFirst()
            .orElse(null);

        if (productDetail == null) {
            productDetailList.add(detail);
        } else {
            throw new CustomException(ErrorType.DUPLICATED_DETAIL);
        }

    }

    public void updateImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void addDetailList(List<ProductDetail> detail) {
        productDetailList.addAll(detail);
    }

    public void increaseWish() {
        wishCount++;
    }

    public void decreaseWish() {
        wishCount--;
    }

    public void increaseReview() {
        reviewCount++;
    }

    public void decreaseReview() {
        reviewCount++;
    }

}
