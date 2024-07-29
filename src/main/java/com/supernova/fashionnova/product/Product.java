package com.supernova.fashionnova.product;

import com.supernova.fashionnova.global.common.Timestamped;
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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
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
    public void addDetail(List<ProductDetail> detail) {
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

}
