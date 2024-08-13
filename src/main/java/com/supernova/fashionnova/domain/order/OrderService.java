package com.supernova.fashionnova.domain.order;

import com.supernova.fashionnova.domain.cart.Cart;
import com.supernova.fashionnova.domain.cart.CartRepository;
import com.supernova.fashionnova.domain.delivery.DeliveryStatus;
import com.supernova.fashionnova.domain.order.dto.AllOrderResponseDto;
import com.supernova.fashionnova.domain.order.dto.OrderRequestDto;
import com.supernova.fashionnova.domain.product.Product;
import com.supernova.fashionnova.domain.product.ProductDetail;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

  private final CartRepository cartRepository;
  private final OrdersRepository ordersRepository;
  private final OrderDetailRepository orderDetailRepository;

    @Transactional
    public AllOrderResponseDto createOrder(OrderRequestDto orderRequestDto, User user) {

        // 송장번호 랜덤 생성 (12자리 수)
        Long invoice =
            Math.abs(UUID.randomUUID().getLeastSignificantBits()) % 900000000000L + 100000000000L;

        // 카트에 담긴 상품 없음
        List<Cart> cartList = cartRepository.findAllByUserId(user.getId());
        if (cartList.isEmpty()) {
            throw new CustomException(ErrorType.CART_EMPTY);
        }

        long totalPrice = cartList.stream().mapToLong(Cart::getTotalPrice).sum();
        Long totalAmount = totalPrice - orderRequestDto.getDiscount()
            - orderRequestDto.getUsedMileage();

        Order order = Order.builder()
            .cost(totalPrice)
            .orderStatus(OrderStatus.Progress)
            .deliveryStatus(DeliveryStatus.BEFORE)
            .address(orderRequestDto.getAddress())
            .user(user)
            .discount(orderRequestDto.getDiscount())
            .usedMileage(orderRequestDto.getUsedMileage())
            .totalPrice(totalAmount)
            .invoice(invoice)
            .build();

        // 영속성 컨텍스트 사용
        order = ordersRepository.save(order);

        List<OrderDetail> orderDetailList = createOrderDetail(cartList, user, order);

        List<OrderDetail> savedOrderDetailList = orderDetailRepository.saveAll(orderDetailList);

        OrderDetail orderName =
            orderDetailRepository.findFirstByOrderIdOrderByProductNameAsc(order.getId()).orElseThrow(
            () -> new CustomException(ErrorType.NOT_FOUND_ORDER));

        int cartCount = cartRepository.countByUserId(user.getId());

        if (cartCount == 0) {
            throw new CustomException(ErrorType.CART_EMPTY);
        }
        order.setOrderPerfect(orderName.getProductName(), cartCount);
        // order.setOrderDetailList(orderDetailList); <- cascade 를 이미 했기 때문에 편의메서드 사용 안 해도 됨
        // orderRepository.save() <- 없어도 됨 - dirty checking 을 해주기 때문

        return AllOrderResponseDto.fromOrder(order, savedOrderDetailList);
    }

    public List<OrderDetail> createOrderDetail(List<Cart> cartList, User user, Order order) {

        List<ProductDetail> productDetailList = cartList.stream().map(Cart::getProductDetail).toList();
        List<Product> productList = productDetailList.stream().map(ProductDetail::getProduct).toList();
        List<OrderDetail> orderDetailList = new ArrayList<>();

        for (int i = 0; i < cartList.size(); i++) {
            Product product = productList.get(i);
            ProductDetail productDetail = productDetailList.get(i);

            orderDetailList.add(OrderDetail.builder()
                .price(product.getPrice())
                .count(cartList.get(i).getCount())
                .productName(product.getProduct())
                .order(order)
                .productDetail(productDetail)
                .user(user)
                .product(product)
                .build()
            );
        }

        return orderDetailList;
    }

    @Transactional
    public List<Order> getOrder(User user) {

        // 주문 내역 없음
        List<Order> order = ordersRepository.findAllByUserId(user.getId());
        if (order.isEmpty()) {
            throw new CustomException(ErrorType.NOT_FOUND_ORDER);
        }

        List<Order> showOrder = new ArrayList<>();
        for (Order filterOrder : order) {
            if (filterOrder.getOrderStatus().equals(OrderStatus.Progress)) {
                ordersRepository.delete(filterOrder);
            } else {
                showOrder.add(filterOrder);
            }
        }

        return showOrder;
    }

  /**
   * 결제 성공 후 주문 상태 변경
   */
  @Transactional
  public void updateOrderStatus(Order order) {
    order.setOrderStatus(OrderStatus.SUCCESS);
  }

  public Order getOrderById(Long orderId) {
    return ordersRepository.findById(orderId).orElseThrow(
        ()-> new CustomException(ErrorType.NOT_FOUND_ORDER));
  }
}
