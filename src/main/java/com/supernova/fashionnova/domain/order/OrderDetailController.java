package com.supernova.fashionnova.domain.order;

import com.supernova.fashionnova.domain.order.dto.OrderDetailResponseDto;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import com.supernova.fashionnova.global.util.ResponseUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
     *
     * @param orderId
     * @param userDetails
     * @return List<OrderDetailResponseDto>
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderDetailResponseDto>> getOrderDetailList(
        @PathVariable Long orderId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseUtil.of(HttpStatus.OK, orderDetailService.getOrderDetail(orderId, userDetails.getUser())
            .stream()
            .map(orderDetail ->
                new OrderDetailResponseDto(
                orderDetail.getId(),
                orderDetail.getCount(),
                orderDetail.getProductName(),
                orderDetail.getPrice(),
                orderDetail.getProductDetail().getSize(),
                orderDetail.getProductDetail().getColor(),
                orderDetail.getOrder().getOrderStatus(),
                orderDetail.getProduct().getImageUrl()
                ))
            .toList());
    }

}
