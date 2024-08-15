package com.supernova.fashionnova.domain.order;

import com.supernova.fashionnova.domain.order.dto.AllOrderResponseDto;
import com.supernova.fashionnova.domain.order.dto.OrderRequestDto;
import com.supernova.fashionnova.domain.order.dto.OrderResponseDto;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import com.supernova.fashionnova.global.util.ResponseUtil;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 결제 전 주문내역, 상세주문내역 생성
     */
    @PostMapping
    public ResponseEntity<AllOrderResponseDto> createOrder(
        @Valid @RequestBody OrderRequestDto orderRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        AllOrderResponseDto allOrderResponseDto = orderService.createOrder(orderRequestDto, userDetails.getUser());

        return ResponseUtil.of(HttpStatus.CREATED, allOrderResponseDto);
    }

    /**
     * 결제 후 주문내역 조회
     */
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrderList(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        log.info(String.valueOf(userDetails.getUser().getId()));

        return ResponseUtil.of(HttpStatus.OK,
            orderService.getOrder(
                    userDetails.getUser()
                )
                .stream()
                .map(order ->
                    new OrderResponseDto(
                        order.getId(),
                        order.getOrderStatus(),
                        order.getAddress(),
                        order.getCost(),
                        order.getDeliveryStatus(),
                        order.getDiscount(),
                        order.getTotalPrice(),
                        order.getUsedMileage(),
                        order.getCreatedAt()))
                .collect(Collectors.toList())
        );
    }

    /**
     * 단건 주문내역 조회
     *
     * @param userDetails
     * @param orderId
     * @return
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long orderId) {

        OrderResponseDto responseDto = orderService.getOrderResponse(orderId,userDetails.getUser());

        return ResponseUtil.of(HttpStatus.OK,responseDto);
    }

}
