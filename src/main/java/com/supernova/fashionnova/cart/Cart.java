package com.supernova.fashionnova.cart;

import com.supernova.fashionnova.product.ProductDetail;
import com.supernova.fashionnova.user.User;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "cart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int count = 0;

    @Column(nullable = false)
    private int totalPrice = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    public Cart(int count, int totalPrice, User user, ProductDetail productDetail) {
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
    public void incrementTotalPrice(int price) {
        this.totalPrice += price;
    }

    public void updateCountPrice(int newCount) {
        this.count = newCount;
        this.totalPrice = newCount * this.productDetail.getProduct().getPrice();
    }

    public void setProductDetail(ProductDetail newProductDetail) {
        this.productDetail = newProductDetail;
        this.totalPrice = this.count * newProductDetail.getProduct().getPrice();
    }

}
