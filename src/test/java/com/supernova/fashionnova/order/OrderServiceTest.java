package com.supernova.fashionnova.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.supernova.fashionnova.domain.cart.Cart;
import com.supernova.fashionnova.domain.cart.CartRepository;
import com.supernova.fashionnova.domain.delivery.DeliveryStatus;
import com.supernova.fashionnova.domain.order.Order;
import com.supernova.fashionnova.domain.order.OrderDetail;
import com.supernova.fashionnova.domain.order.OrderDetailRepository;
import com.supernova.fashionnova.domain.order.OrderService;
import com.supernova.fashionnova.domain.order.OrderStatus;
import com.supernova.fashionnova.domain.order.OrdersRepository;
import com.supernova.fashionnova.domain.order.dto.AllOrderResponseDto;
import com.supernova.fashionnova.domain.order.dto.OrderRequestDto;
import com.supernova.fashionnova.domain.product.Product;
import com.supernova.fashionnova.domain.product.ProductCategory;
import com.supernova.fashionnova.domain.product.ProductDetail;
import com.supernova.fashionnova.domain.product.ProductStatus;
import com.supernova.fashionnova.domain.user.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

class OrderServiceTest {
  @Mock
  private CartRepository cartRepository;

  @Mock
  private OrdersRepository ordersRepository;

  @Mock
  private OrderDetailRepository orderDetailRepository;

  @Spy
  @InjectMocks
  private OrderService orderService;

  private User user;
  private Order order;
  private Product product;
  private ProductDetail productDetail;
  private OrderDetail orderDetail;

  @BeforeEach
  void setUp() {
    // 모의 객체 초기화
    MockitoAnnotations.openMocks(this);
    // 수동으로 OrderService 인스턴스 생성 및 Mock 객체 주입
    orderService = Mockito.spy(new OrderService(cartRepository, ordersRepository, orderDetailRepository));
    this.user = User.builder()
        .userName("testUser1234")
        .name("테스트유저")
        .password("test1234!#")
        .email("test@gmail.com")
        .phone("010-1234-5678")
        .build();
    this.product = Product.builder()
        .category(ProductCategory.TOP)
        .explanation("explanation")
        .price(10000L)
        .productStatus(ProductStatus.ACTIVE)
        .product("product")
        .build();
    this.productDetail = ProductDetail.builder()
        .color("Black")
        .quantity(100L)
        .size("S")
        .product(product)
        .build();
  }

  @Test
  @DisplayName("주문 생성 테스트")
  void createOrder() {
    //given
    OrderRequestDto orderRequestDto = new OrderRequestDto(
       10000L,
        1000,
        1000L,
        "address",
        8000L
    );
    List<Cart> cartList = new ArrayList<>();

    Cart cart1 = Cart.builder()
        .count(1)
        .totalPrice(10000L)
        .user(user)
        .productDetail(productDetail)
        .build();

    Cart cart2 = Cart.builder()
        .count(1)
        .totalPrice(10000L)
        .user(user)
        .productDetail(productDetail)
        .build();

    cartList.add(cart1);
    cartList.add(cart2);
    when(cartRepository.findAllByUserId(user.getId())).thenReturn(cartList);

    when(ordersRepository.save(any(Order.class))).thenAnswer(invocation -> {
      Order order = invocation.getArgument(0);
      setField(order, "id", 1L);  // order 객체에 id를 설정
      return order;
    });

    // order 객체를 ordersRepository.save() 후에 할당
    Order order = Order.builder()
        .user(user)
        .totalPrice(18000L)
        .cost(20000L)
        .address("address")
        .orderStatus(OrderStatus.PROGRESS)
        .discount(1000)
        .invoice(123L)
        .usedMileage(1000L)
        .deliveryStatus(DeliveryStatus.BEFORE)
        .build();

    List<OrderDetail> mockedOrderDetails = new ArrayList<>();
    mockedOrderDetails.add(OrderDetail.builder()
            .count(1)
            .price(10000L)
            .productName("productName1")
            .order(order)
            .product(product)
            .user(user)
            .productDetail(productDetail)
        .build()
    );
    mockedOrderDetails.add(OrderDetail.builder()
        .count(1)
        .price(10000L)
        .productName("productName2")
        .order(order)
        .product(product)
        .user(user)
        .productDetail(productDetail)
        .build()
    );
    doReturn(mockedOrderDetails).when(orderService).createOrderDetail(anyList(), any(User.class), any(Order.class));
    when(orderDetailRepository.saveAll(mockedOrderDetails)).thenReturn(mockedOrderDetails);
    when(ordersRepository.save(any(Order.class))).thenReturn(order);
    when(orderDetailRepository.findFirstByOrderIdOrderByProductNameAsc(order.getId())).thenReturn(
        Optional.of(mockedOrderDetails.get(0)));
    when(cartRepository.countByUserId(user.getId())).thenReturn(cartList.size());
    // when
    AllOrderResponseDto response = orderService.createOrder(orderRequestDto, user);

    // then
    assertNotNull(response);
    assertThat(response.getOrderId()).isEqualTo(order.getId());
    assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.PROGRESS);
    assertThat(response.getAddress()).isEqualTo(orderRequestDto.getAddress());
    assertThat(response.getCost()).isEqualTo(20000L);
    assertThat(response.getDeliveryStatus()).isEqualTo(DeliveryStatus.BEFORE);
    assertThat(response.getDiscount()).isEqualTo(orderRequestDto.getDiscount());
    assertThat(response.getTotalPrice()).isEqualTo(18000L);
    assertThat(response.getUsedMileage()).isEqualTo(1000L);
    assertThat(response.getOrderName()).isEqualTo("productName1");
    assertThat(response.getCartCount()).isEqualTo(2);
    assertThat(response.getOrderDetailList()).hasSize(2);
    assertThat(response.getOrderDetailList().get(0).getProductName()).isEqualTo("productName1");

    verify(cartRepository, times(1)).findAllByUserId(user.getId());
    verify(ordersRepository, times(1)).save(any(Order.class));
    verify(orderDetailRepository, times(1)).saveAll(any(List.class));
    verify(orderDetailRepository, times(1)).findFirstByOrderIdOrderByProductNameAsc(order.getId());
    verify(cartRepository, times(1)).countByUserId(user.getId());
  }

  @Test
  @DisplayName("주문상세 생성 테스트")
  void createOrderDetail() {
    Product product2 = Product.builder()
        .category(ProductCategory.TOP)
        .explanation("explanation")
        .price(10000L)
        .productStatus(ProductStatus.ACTIVE)
        .product("product2")
        .build();

    ProductDetail productDetail2 = ProductDetail.builder()
        .color("Black")
        .quantity(100L)
        .size("S")
        .product(product2)
        .build();

    Cart cart1 = Cart.builder()
        .count(1)
        .totalPrice(10000L)
        .user(user)
        .productDetail(productDetail)
        .build();

    Cart cart2 = Cart.builder()
        .count(2)
        .totalPrice(10000L)
        .user(user)
        .productDetail(productDetail2)
        .build();

    List<Cart> cartList = new ArrayList<>();
    cartList.add(cart1);
    cartList.add(cart2);

    //when
    List<OrderDetail> result = orderService.createOrderDetail(cartList, user, order);

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());

    // 첫 번째 OrderDetail 검증
    OrderDetail orderDetail1 = result.get(0);
    assertEquals(10000L, orderDetail1.getPrice());
    assertEquals(1, orderDetail1.getCount());
    assertEquals("product", orderDetail1.getProductName());
    assertEquals(user, orderDetail1.getUser());
    assertEquals(order, orderDetail1.getOrder());
    assertEquals(cartList.get(0).getProductDetail(), orderDetail1.getProductDetail());
    assertEquals(cartList.get(0).getProductDetail().getProduct(), orderDetail1.getProduct());

    // 두 번째 OrderDetail 검증
    OrderDetail orderDetail2 = result.get(1);
    assertEquals(10000L, orderDetail2.getPrice());
    assertEquals(2, orderDetail2.getCount());
    assertEquals("product2", orderDetail2.getProductName());
    assertEquals(user, orderDetail2.getUser());
    assertEquals(order, orderDetail2.getOrder());
    assertEquals(cartList.get(1).getProductDetail(), orderDetail2.getProductDetail());
    assertEquals(cartList.get(1).getProductDetail().getProduct(), orderDetail2.getProduct());

  }

//  @Test
//  @DisplayName("주문 조회 테스트")
//  void getOrder() {
//    //given
//    Order order1 = Order.builder()
//        .user(user)
//        .totalPrice(18000L)
//        .cost(20000L)
//        .address("address1")
//        .orderStatus(OrderStatus.SUCCESS)
//        .discount(1000)
//        .invoice(123L)
//        .usedMileage(1000L)
//        .deliveryStatus(DeliveryStatus.BEFORE)
//        .build();
//
//    Order order2 = Order.builder()
//        .user(user)
//        .totalPrice(18000L)
//        .cost(20000L)
//        .address("address2")
//        .orderStatus(OrderStatus.PROGRESS)
//        .discount(1000)
//        .invoice(124L)
//        .usedMileage(1000L)
//        .deliveryStatus(DeliveryStatus.BEFORE)
//        .build();
//
//    List<Order> orderList = new ArrayList<>();
//    orderList.add(order1);
//    orderList.add(order2);
//    when(ordersRepository.findAllByUserId(user.getId())).thenReturn(orderList);
//
//    // When
//    List<Order> result = orderService.getOrder(user);
//
//    // Then
//    assertNotNull(result);
//    assertEquals(2, result.size());
//    assertEquals(OrderStatus.SUCCESS, result.get(0).getOrderStatus());
//    verify(ordersRepository, times(1)).delete(any(Order.class));
//  }

}