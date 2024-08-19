/*
package com.supernova.fashionnova.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.domain.delivery.DeliveryStatus;
import com.supernova.fashionnova.domain.order.Order;
import com.supernova.fashionnova.domain.order.OrderController;
import com.supernova.fashionnova.domain.order.OrderDetail;
import com.supernova.fashionnova.domain.order.OrderService;
import com.supernova.fashionnova.domain.order.OrderStatus;
import com.supernova.fashionnova.domain.order.dto.AllOrderResponseDto;
import com.supernova.fashionnova.domain.order.dto.OrderRequestDto;
import com.supernova.fashionnova.domain.product.Product;
import com.supernova.fashionnova.domain.product.ProductCategory;
import com.supernova.fashionnova.domain.product.ProductDetail;
import com.supernova.fashionnova.domain.product.ProductStatus;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.domain.user.UserService;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
class OrderControllerTest {
  @Autowired
  private MockMvc mockMvc;  // MockMvc를 자동 주입하여 컨트롤러를 테스트할 때 HTTP 요청을 보내고 응답을 받을 수 있습니다.

  @Autowired
  private ObjectMapper objectMapper;  // JSON 직렬화 및 역직렬화를 위한 Jackson 라이브러리의 객체입니다.

  @MockBean
  private UserService service;  // UserService를 목(Mock) 객체로 만들어 UserController의 의존성을 주입합니다.

  @MockBean
  private OrderService orderService;

  private final String baseUrl = "/orders";  // 테스트할 기본 URL을 설정합니다.
  UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);

  @BeforeEach
  void setUp() {

    given(userDetails.getUsername()).willReturn("user");
    given(userDetails.getUser()).willReturn(new User(
        "testUSer",
        "Test1234!@",
        "테스트유저",
        "test@gmail.com",
        "010-1234-5678"
    ));

    SecurityContextHolder.setContext(new SecurityContextImpl());
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities()));
  }

  @Test
  @DisplayName("주문 생성 테스트")
  void createOrderTest() throws Exception {
    //given
    OrderRequestDto orderRequestDto = new OrderRequestDto(
      10000L,
        1000,
        1000L,
        "address",
        8000L
    );
    List<OrderDetail> savedOrderDetailList = getOrderDetails();
    AllOrderResponseDto allOrderResponseDto = new AllOrderResponseDto(
      1L,
      OrderStatus.PROGRESS,
      "address",
      10000L,
      DeliveryStatus.BEFORE,
      1000,
      8000L,
      1000L,
      LocalDateTime.now(),
      savedOrderDetailList,
      "order1",
      1
    );

    //when
    given(orderService.createOrder(any(OrderRequestDto.class), any())).willReturn(allOrderResponseDto);

    // Then
    mockMvc.perform(post(baseUrl)
            .with(csrf())
            .content(objectMapper.writeValueAsString(orderRequestDto))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpectAll(
            status().isCreated(),  // 응답 상태 코드가 201 Created인지 확인합니다.
            content().contentType(MediaType.APPLICATION_JSON),  // 응답 콘텐츠 타입이 JSON인지 확인합니다.
            jsonPath("$.orderId").value(allOrderResponseDto.getOrderId()),  // 응답 JSON에 orderId 필드가 올바른지 확인합니다.
            jsonPath("$.orderName").value(allOrderResponseDto.getOrderName())  // 응답 JSON에 orderName 필드가 올바른지 확인합니다.
        );
  }

  private List<OrderDetail> getOrderDetails() {
    User user = userDetails.getUser();
    Order order = new Order(user, 8000L, 1000L, 1000, 1000L, "address", 1234L, DeliveryStatus.BEFORE, OrderStatus.PROGRESS);
    Product product = new Product("product", 10000L, "explanation", ProductCategory.TOP,
        ProductStatus.ACTIVE);
    ProductDetail productDetail = new ProductDetail("S", "BLACK", 100L, product);
    List<OrderDetail> savedOrderDetailList = List.of(
        new OrderDetail(3, "productName", 10000L, user, order, product, productDetail)
    );
    return savedOrderDetailList;
  }

  @Test
  @DisplayName("주문 조회 테스트")
  void getOrderListTest() throws Exception {

    //given
    User user = userDetails.getUser();
    Order order1 = new Order(
        user,
        8000L,
        10000L,
        1000,
        1000L,
        "address1",
        32213L,
        DeliveryStatus.BEFORE,
        OrderStatus.SUCCESS
    );
    Order order2 = new Order(
        user,
        8000L,
        10000L,
        1000,
        1000L,
        "address2",
        32214L,
        DeliveryStatus.BEFORE,
        OrderStatus.SUCCESS
    );

    //when
    List<Order> orderList = List.of(order1, order2);
    given(orderService.getOrder(user)).willReturn(orderList);

    // Then
    mockMvc.perform(get(baseUrl)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))// @AuthenticationPrincipal이므로 principal로 전달
        .andExpect(status().isOk()) // HTTP 200 상태 코드 확인
        .andExpect(jsonPath("$.length()").value(orderList.size())) // 반환된 리스트 크기 확인
        .andExpect(jsonPath("$[0].orderId").value(order1.getId())) // 첫 번째 주문의 orderId 확인
        .andExpect(jsonPath("$[0].address").value(order1.getAddress())) // 첫 번째 주문의 주소 확인
        .andExpect(jsonPath("$[1].orderId").value(order2.getId())) // 두 번째 주문의 orderId 확인
        .andExpect(jsonPath("$[1].address").value(order2.getAddress())); // 두 번째 주문의 주소 확인
  }
}
 */