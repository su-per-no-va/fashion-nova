package com.supernova.fashionnova.domain.order;

import com.supernova.fashionnova.domain.product.Product;
import com.supernova.fashionnova.domain.product.ProductDetail;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.global.common.Timestamped;
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
import lombok.Setter;

@Entity
@Getter
@Table(name = "orders_details")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetail extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Long price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

    @Builder
    public OrderDetail(int count, String productName, Long price, User user, Order order, Product product, ProductDetail productDetail) {
        this.count = count;
        this.productName = productName;
        this.price = price;
        this.user = user;
        this.order = order;
        this.product = product;
        this.productDetail = productDetail;
    }

}
