package com.supernova.fashionnova.review;

import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.user.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByUser(User user, Pageable pageable);

    Optional<Review> findByIdAndUser(Long id, User user);

    Page<Review> findByProduct(Product product, Pageable pageable);
}
