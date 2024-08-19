/*
package com.supernova.fashionnova.order;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.domain.delivery.DeliveryStatus;
import com.supernova.fashionnova.domain.order.Order;
import com.supernova.fashionnova.domain.order.OrderDetail;
import com.supernova.fashionnova.domain.order.OrderDetailController;
import com.supernova.fashionnova.domain.order.OrderDetailService;
import com.supernova.fashionnova.domain.order.OrderStatus;
import com.supernova.fashionnova.domain.product.Product;
import com.supernova.fashionnova.domain.product.ProductCategory;
import com.supernova.fashionnova.domain.product.ProductDetail;
import com.supernova.fashionnova.domain.product.ProductStatus;
import com.supernova.fashionnova.domain.review.Review;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import java.util.ArrayList;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderDetailController.class)
class OrderDetailControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private OrderDetailService orderDetailService;

  private final String baseUrl = "/orderDetails";  // 테스트할 기본 URL을 설정합니다.
  UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);

  @BeforeEach
  void setUp() {
    given(userDetails.getUsername()).willReturn("testUser");
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
  @DisplayName("주문 상세 조회 테스트")
  void getOrderDetailList() throws Exception {
    //given
    User user = userDetails.getUser();

    Product product = Product.builder()
        .category(ProductCategory.TOP)
        .explanation("explanation")
        .price(10000L)
        .productStatus(ProductStatus.ACTIVE)
        .product("product")
        .build();

    ProductDetail productDetail = ProductDetail.builder()
        .color("Black")
        .quantity(100L)
        .size("S")
        .product(product)
        .build();

    Order order = Mockito.mock(Order.class);

    OrderDetail orderDetail1 = OrderDetail.builder()
        .count(1)
        .price(10000L)
        .productName("product")
        .order(order)
        .product(product)
        .user(user)
        .productDetail(productDetail)
        .build();

    OrderDetail orderDetail2 = OrderDetail.builder()
        .count(1)
        .price(10000L)
        .productName("product")
        .order(order)
        .product(product)
        .user(user)
        .productDetail(productDetail)
        .build();

    List<OrderDetail> orderDetailList = new ArrayList<>();
    orderDetailList.add(orderDetail1);
    orderDetailList.add(orderDetail2);

    given(order.getId()).willReturn(1L);
    given(order.getOrderName()).willReturn("testOrder");
    given(order.getOrderDetailList()).willReturn(orderDetailList);
    given(order.getOrderStatus()).willReturn(OrderStatus.SUCCESS);
    given(order.getAddress()).willReturn("testAddress");

    // When
    given(orderDetailService.getOrderDetail(order.getId(), user)).willReturn(orderDetailList);

    // Then
    mockMvc.perform(get(baseUrl + "/" + order.getId())
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()) // HTTP 200 상태 코드 확인
        .andExpect(jsonPath("$.length()").value(orderDetailList.size())) // 반환된 리스트 크기 확인
        .andExpect(jsonPath("$[0].id").value(orderDetail1.getId())) // 첫 번째 주문 상세의 id 확인
        .andExpect(jsonPath("$[0].productName").value(orderDetail1.getProductName())) // 첫 번째 주문 상세의 제품명 확인
        .andExpect(jsonPath("$[0].price").value(orderDetail1.getPrice())) // 첫 번째 주문 상세의 가격 확인
        .andExpect(jsonPath("$[0].count").value(orderDetail1.getCount())) // 첫 번째 주문 상세의 수량 확인
        .andExpect(jsonPath("$[0].size").value(orderDetail1.getProductDetail().getSize())) // 첫 번째 주문 상세의 제품 사이즈 확인
        .andExpect(jsonPath("$[0].color").value(orderDetail1.getProductDetail().getColor())) // 첫 번째 주문 상세의 제품 색상 확인
        .andExpect(jsonPath("$[0].orderStatus").value(orderDetail1.getOrder().getOrderStatus().toString())) // 첫 번째 주문의 상태 확인
        .andExpect(jsonPath("$[1].id").value(orderDetail2.getId())) // 두 번째 주문 상세의 id 확인
        .andExpect(jsonPath("$[1].productName").value(orderDetail2.getProductName())) // 두 번째 주문 상세의 제품명 확인
        .andExpect(jsonPath("$[1].price").value(orderDetail2.getPrice())) // 두 번째 주문 상세의 가격 확인
        .andExpect(jsonPath("$[1].count").value(orderDetail2.getCount())) // 두 번째 주문 상세의 수량 확인
        .andExpect(jsonPath("$[1].size").value(orderDetail2.getProductDetail().getSize())) // 두 번째 주문 상세의 제품 사이즈 확인
        .andExpect(jsonPath("$[1].color").value(orderDetail2.getProductDetail().getColor())) // 두 번째 주문 상세의 제품 색상 확인
        .andExpect(jsonPath("$[1].orderStatus").value(orderDetail2.getOrder().getOrderStatus().toString())); // 두 번째 주문의 상태 확인
  }
}

 */