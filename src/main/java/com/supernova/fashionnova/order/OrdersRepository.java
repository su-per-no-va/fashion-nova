package com.supernova.fashionnova.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrdersRepository extends JpaRepository<Order, Long> {

    @Query("SELECT COUNT(o) > 0 FROM Order o JOIN OrderDetail od ON o.id = od.id WHERE o.user.id = :userId AND od.product.id = :productId")
    boolean existsByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

}