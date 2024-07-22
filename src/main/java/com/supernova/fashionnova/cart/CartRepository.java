package com.supernova.fashionnova.cart;

import com.supernova.fashionnova.product.ProductDetail;
import com.supernova.fashionnova.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUser(User user);

    Optional<Cart> findByUserAndProductDetail(User user, ProductDetail productDetail);

}
