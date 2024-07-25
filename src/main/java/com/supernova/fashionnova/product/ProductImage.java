package com.supernova.fashionnova.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "product_image")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productImageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    public ProductImage(Product product, String productImageUrl) {
        this.product = product;
        this.productImageUrl = productImageUrl;
    }

}
