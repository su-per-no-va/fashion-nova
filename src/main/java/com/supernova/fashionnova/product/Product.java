package com.supernova.fashionnova.product;

import com.supernova.fashionnova.global.common.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
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
    private String classification;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private int like_count;

    @Column(nullable = false)
    private int review_count;

    @Column(nullable = false)
    private String product_status;


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

    public Product(String product, int price, String explanation, String classification,
        String category, int like_count, int review_count, String product_status) {
        this.product = product;
        this.price = price;
        this.explanation = explanation;
        this.classification = classification;
        this.category = category;
        this.like_count = like_count;
        this.review_count = review_count;
        this.product_status = product_status;
    }
}



