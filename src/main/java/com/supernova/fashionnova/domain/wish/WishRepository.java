package com.supernova.fashionnova.domain.wish;

import com.supernova.fashionnova.domain.product.Product;
import com.supernova.fashionnova.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {

    Page<Wish> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    boolean existsByUserAndProduct(User user, Product product);

}
