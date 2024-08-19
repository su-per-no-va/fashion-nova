package com.supernova.fashionnova.domain.cart;

import com.supernova.fashionnova.domain.product.ProductDetail;
import com.supernova.fashionnova.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUser(User user);

    Optional<Cart> findByUserAndProductDetail(User user, ProductDetail productDetail);

    List<Cart> findAllByUserId(Long id);

    int countByUserId(Long orderId);

}
