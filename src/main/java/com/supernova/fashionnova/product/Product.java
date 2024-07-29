package com.supernova.fashionnova.product;

import com.supernova.fashionnova.global.common.Timestamped;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private int price;

    private String explanation;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private int reviewCount;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDetail> productDetailList = new ArrayList<>();

/*
    @OneToMany(mappedBy = "product")
    private List<Wish> wishList = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "product_image_id")
    private List<ProductImage> imageList = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductDetail> productDetail = new ArrayList<>();

*/
    public void addDetail(ProductDetail detail) {
        ProductDetail productDetail = productDetailList.stream()
            .filter(p -> p.getColor().equals(detail.getColor()) && p.getSize().equals(detail.getSize())).findFirst().orElse(null);
        if(productDetail == null) {
            productDetailList.add(detail);
        }else{
            throw new CustomException(ErrorType.DUPLICATED_DETAIL);
        }

    }

    public void addDetailList(List<ProductDetail> detail) {
        productDetailList.addAll(detail);
    }


    @Builder
    public Product(String product, int price, String explanation, ProductCategory category, ProductStatus productStatus) {
        this.product = product;
        this.price = price;
        this.explanation = explanation;
        this.category = category;
        this.productStatus = productStatus;
        this.likeCount = 0;
        this.reviewCount = 0;
    }

    public void updateProductDetails(List<ProductDetail> newDetails) {
        for (ProductDetail newDetail : newDetails) {
            ProductDetail existingDetail = productDetailList.stream()
                .filter(detail -> detail.getId().equals(newDetail.getId()))
                .findFirst()
                .orElse(null);

            if (existingDetail != null) {
                existingDetail.updateDetail(newDetail.getSize(), newDetail.getColor(), newDetail.getQuantity(), newDetail.getStatus());
            }
        }

    }

    public void updateProduct(String product, int price, String explanation, ProductCategory category, ProductStatus productStatus) {
        this.product = product;
        this.price = price;
        this.explanation = explanation;
        this.category = category;
        this.productStatus = productStatus;
    }
}
