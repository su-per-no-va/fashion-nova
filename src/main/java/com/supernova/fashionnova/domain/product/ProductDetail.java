package com.supernova.fashionnova.domain.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "productDetail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Builder
    public ProductDetail(String size, String color, Long quantity, Product product) {
        this.size = size;
        this.color = color;
        this.quantity = quantity;
        this.status = ProductStatus.ACTIVE;
        this.product = product;
    }

    public void updateDetail(String size, String color, Long quantity, ProductStatus status) {
        this.size = size;
        this.color = color;
        this.quantity = quantity;
        this.status = status;
    }

    public void updateQuantity(Long quantity) {
        this.quantity = quantity;
    }

}
