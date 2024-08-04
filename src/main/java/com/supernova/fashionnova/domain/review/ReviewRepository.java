package com.supernova.fashionnova.domain.review;

import com.supernova.fashionnova.domain.product.Product;
import com.supernova.fashionnova.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByUser(User user, Pageable pageable);

    Optional<Review> findByIdAndUser(Long id, User user);

    List<Review> findByProduct(Product product, Pageable pageable);

}
