package com.supernova.fashionnova.order;

import com.supernova.fashionnova.global.util.ResponseUtil;
import com.supernova.fashionnova.order.dto.OrderDetailResponseDto;
import com.supernova.fashionnova.order.dto.OrderResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orderDetails")
public class OrderDetailController {

  private final OrderDetailService orderDetailService;

  /**
   * 주문상세내역 조회
   * */
  @GetMapping("/{orderId}")
  public ResponseEntity<List<OrderDetailResponseDto>> getOrderDetailList(@PathVariable Long orderId) {
    return ResponseUtil.of(HttpStatus.OK, orderDetailService.getOrderDetail(orderId).stream().map(orderDetail -> new OrderDetailResponseDto(orderDetail.getId(), orderDetail.getCount(), orderDetail.getProductName(), orderDetail.getPrice())).toList());
  }
}
