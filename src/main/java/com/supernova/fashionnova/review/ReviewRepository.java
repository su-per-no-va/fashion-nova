package com.supernova.fashionnova.review;

import com.supernova.fashionnova.product.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProduct(Product product);

}
