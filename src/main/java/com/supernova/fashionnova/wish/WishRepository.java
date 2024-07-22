package com.supernova.fashionnova.wish;

import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.user.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Page<Wish> findByUser(User user, Pageable pageable);

    Optional<Wish> findByUserAndProduct(User user, Product product);
}
