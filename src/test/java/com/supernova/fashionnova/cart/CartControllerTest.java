/*
package com.supernova.fashionnova.cart;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.domain.cart.CartController;
import com.supernova.fashionnova.domain.cart.CartService;
import com.supernova.fashionnova.domain.cart.dto.CartDeleteRequestDto;
import com.supernova.fashionnova.domain.cart.dto.CartRequestDto;
import com.supernova.fashionnova.domain.cart.dto.CartResponseDto;
import com.supernova.fashionnova.domain.cart.dto.CartUpdateRequestDto;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import java.util.Collections;
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
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    private final String baseUrl = "/carts";
    UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);

    @BeforeEach
    void setUp() {
        // Given a mock UserDetailsImpl
        given(userDetails.getUsername()).willReturn("user");
        given(userDetails.getUser()).willReturn(new User(
            "testUSer",
            "Test1234!@",
            "테스트유저",
            "test@gmail.com",
            "010-1234-5678"
        ));

        // Set the security context
        SecurityContextHolder.setContext(new SecurityContextImpl());
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities()));
    }

    @Test
    @DisplayName("장바구니에 상품 추가 테스트")
    void addToCartSuccess() throws Exception {
        // given
        CartRequestDto requestDto = new CartRequestDto(
            1L,
            2,
            "M",
            "BLACK");

        doNothing().when(cartService).addCart(any(User.class), any(CartRequestDto.class));

        // when
        ResultActions result = mockMvc.perform(post(baseUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .with(csrf())
            .principal(() -> userDetails.getUsername()));

        // then
        result.andExpect(status().isOk())
            .andExpect(content().string("장바구니 담기 완료"));
        verify(cartService).addCart(any(User.class), any(CartRequestDto.class));
    }

    @Test
    @DisplayName("장바구니 조회 테스트")
    void getCartTest() throws Exception {
        // given
        CartResponseDto responseDto = new CartResponseDto(Collections.emptyList(), 0L);

        given(cartService.getCart(any(User.class))).willReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(get(baseUrl)
            .with(csrf())
            .principal(() -> userDetails.getUsername()));

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.cartItemDtoList").isEmpty())
            .andExpect(jsonPath("$.totalPrice").value(0));
        verify(cartService).getCart(any(User.class));
    }

    @Test
    @DisplayName("장바구니 수정 테스트")
    void updateCartSuccess() throws Exception {
        // given
        CartUpdateRequestDto requestDto = new CartUpdateRequestDto(
            1L,
            2,
            "L",
            "GRAY"
        );

        doNothing().when(cartService).updateCart(any(User.class), any(CartUpdateRequestDto.class));

        // when
        ResultActions result = mockMvc.perform(put(baseUrl)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(requestDto))
            .with(csrf())
            .principal(() -> userDetails.getUsername()));

        // then
        result.andExpect(status().isOk())
            .andExpect(content().string("상품 옵션 수정 완료"));
        verify(cartService).updateCart(any(User.class), any(CartUpdateRequestDto.class));
    }

    @Test
    @DisplayName("장바구니 상품 개별 삭제 테스트")
    void deleteFromCartTest() throws Exception {
        // given
        CartDeleteRequestDto requestDto = new CartDeleteRequestDto(1L);
        doNothing().when(cartService).deleteFromCart(any(User.class), anyLong());

        // when
        ResultActions result = mockMvc.perform(delete(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .with(csrf())
                .principal(() -> userDetails.getUsername()))
            .andDo(print());

        // then
        result.andExpect(status().isOk())
            .andExpect(content().string("장바구니 상품 삭제 완료"));
        verify(cartService).deleteFromCart(any(User.class), anyLong());
    }

    @Test
    @DisplayName("장바구니 비우기 테스트")
    void clearCartTest() throws Exception {
        // given
        User user = Mockito.mock(User.class);
        when(userDetails.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(1L);

        doNothing().when(cartService).clearCart(any(Long.class));

        // when
        ResultActions result = mockMvc.perform(delete(baseUrl + "/delete")
                .with(csrf())
                .principal(() -> userDetails.getUsername()))
            .andDo(print());

        // then
        result.andExpect(status().isOk())
            .andExpect(content().string("장바구니 비우기 완료"));
        verify(cartService).clearCart(user.getId());
    }

}

 */