package com.supernova.fashionnova.domain.order;

import com.supernova.fashionnova.domain.cart.CartRepository;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.domain.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
  private final OrdersRepository ordersRepository;
  private final OrderDetailRepository orderDetailRepository;
  private final CartRepository cartRepository;

  public List<OrderDetail> getOrderDetail(Long orderId, User user) {
    List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrderId(orderId);
    if(orderDetailList.isEmpty()){
      throw new CustomException(ErrorType.NOT_FOUND_ORDER);
    }
    if(!user.getId().equals(orderDetailList.get(0).getUser().getId())){
      throw new CustomException(ErrorType.DENIED_PERMISSION);
    }
    return orderDetailList;
  }
}
