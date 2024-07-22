package com.supernova.fashionnova.cart;

import com.supernova.fashionnova.product.ProductDetail;
import com.supernova.fashionnova.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
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
    private int totalPrice = 0;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    @JoinColumn(name = "product_detail_id")
    private List<ProductDetail> productDetailList = new ArrayList<>();

    // 사용자 설정 메서드
    public void assignUser(User user) {
        if (this.user != null) {
            throw new IllegalStateException("장바구니를 이미 할당받음");
        }
        this.user = user;
    }

    // 상품 수량 증가
    public void incrementCount(int count) {
        this.count += count;
    }

    // totalPrice 증가
    public void incrementTotalPrice(int price) {
        this.totalPrice += price;
    }
}
