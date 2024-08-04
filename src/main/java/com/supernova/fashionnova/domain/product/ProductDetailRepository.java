package com.supernova.fashionnova.domain.product;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {

    Optional<ProductDetail> findByProductAndSizeAndColor(Product product, String size, String color);

}
