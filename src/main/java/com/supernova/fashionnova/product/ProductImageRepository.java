package com.supernova.fashionnova.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {


     List<ProductImage> findAllByProduct(Product product);
}
