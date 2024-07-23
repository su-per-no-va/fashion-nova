package com.supernova.fashionnova.review;

import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProduct(Product product);

    List<Review> findByUser(User user);

    Optional<Review> findByIdAndUser(Long id, User user);

}
