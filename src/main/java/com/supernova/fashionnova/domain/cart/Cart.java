package com.supernova.fashionnova.domain.cart;

import com.supernova.fashionnova.domain.product.ProductDetail;
import com.supernova.fashionnova.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "cart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int count = 0;

    @Column(nullable = false)
    private Long totalPrice = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    @Builder
    public Cart(int count, Long totalPrice, User user, ProductDetail productDetail) {
        this.count = count;
        this.totalPrice = totalPrice;
        this.user = user;
        this.productDetail = productDetail;
    }

    // 상품 수량 증가
    public void incrementCount(int count) {
        this.count += count;
    }

    // totalPrice 증가
    public void incrementTotalPrice(Long price) {
        this.totalPrice += price;
    }

    // 수량과 총가격 업데이트
    public void updateCountPrice(int newCount) {
        this.count = newCount;
        this.totalPrice = newCount * this.productDetail.getProduct().getPrice();
    }

    // 장바구니 상품 상세 정보 업데이트
    public void setProductDetail(ProductDetail newProductDetail) {
        this.productDetail = newProductDetail;
        this.totalPrice = this.count * newProductDetail.getProduct().getPrice();
    }

}
