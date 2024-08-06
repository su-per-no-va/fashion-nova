package com.supernova.fashionnova.domain.order;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findAllByOrderId(Long id);

    Optional<OrderDetail> findFirstByOrderIdOrderByProductNameAsc(Long orderId);

}
