package com.supernova.fashionnova.product;

import com.supernova.fashionnova.global.common.Timestamped;
import com.supernova.fashionnova.review.Review;
import com.supernova.fashionnova.wish.Wish;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private String classification;

    @Column(nullable = false)
    private Long like_count;

    @Column(nullable = false)
    private Long review_count;

    @Column(nullable = false)
    private String product_status;

/*
    @OneToMany(mappedBy = "product")
    private List<Wish> wishList = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductDetail> productDetail = new ArrayList<>();

*/


}



