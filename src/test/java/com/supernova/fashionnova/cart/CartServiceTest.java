package com.supernova.fashionnova.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.supernova.fashionnova.cart.dto.CartItemDto;
import com.supernova.fashionnova.cart.dto.CartRequestDto;
import com.supernova.fashionnova.cart.dto.CartResponseDto;
import com.supernova.fashionnova.cart.dto.CartUpdateRequestDto;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductDetail;
import com.supernova.fashionnova.product.ProductDetailRepository;
import com.supernova.fashionnova.product.ProductRepository;
import com.supernova.fashionnova.user.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductDetailRepository productDetailRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    @Nested
    class AddCartTest {

        @Test
        @DisplayName("장바구니 상품 추가 성공 테스트")
        void AddCartTest1() {
            // given
            User user = mock(User.class);
            CartRequestDto requestDto = new CartRequestDto(
                1L,
                2,
                "M",
                "BLACK"
            );

            Product product = mock(Product.class);
            ProductDetail productDetail = mock(ProductDetail.class);

            given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
            given(productDetailRepository.findByProductAndSizeAndColor(any(Product.class),
                anyString(), anyString())).willReturn(Optional.of(productDetail));
            given(productDetail.getQuantity()).willReturn(10L);
            given(productDetail.getStatus()).willReturn("ACTIVE");

            // when
            assertDoesNotThrow(() -> cartService.addCart(user, requestDto));

            // then
            verify(cartRepository).save(any(Cart.class));
        }

        @Test
        @DisplayName("장바구니 상품 추가 실패 테스트 - 상품 정보 없음")
        void AddCartTest2() {
            // given
            User user = mock(User.class);
            CartRequestDto requestDto = new CartRequestDto(
                1L,
                2,
                "M",
                "black"
            );

            given(productRepository.findById(anyLong())).willReturn(Optional.empty());

            // when / then
            assertThrows(CustomException.class, () -> cartService.addCart(user, requestDto));
        }

        @Test
        @DisplayName("장바구니 추가 실패 테스트 - 품절된 상품")
        void AddCartTest3() {
            // given
            User user = mock(User.class);
            CartRequestDto requestDto = new CartRequestDto(
                1L,
                2,
                "M",
                "BLACK"
            );

            Product product = mock(Product.class);
            ProductDetail productDetail = mock(ProductDetail.class);

            given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
            given(productDetailRepository.findByProductAndSizeAndColor(any(Product.class),
                anyString(), anyString())).willReturn(Optional.of(productDetail));
            given(productDetail.getQuantity()).willReturn(0L);

            // when / then
            assertThrows(CustomException.class, () -> cartService.addCart(user, requestDto));
        }
    }

    @Nested
    class GetCartTest {

        @Test
        @DisplayName("장바구니 조회 테스트")
        void GetCartTest1() {
            // given
            User user = mock(User.class);
            ProductDetail productDetail = mock(ProductDetail.class);
            Product product = mock(Product.class);
            Cart cart = new Cart(
                1,
                100,
                user,
                productDetail);

            given(productDetail.getProduct()).willReturn(product);
            given(product.getProduct()).willReturn("꽃무늬 원피스");
            given(product.getPrice()).willReturn(100);
            given(productDetail.getSize()).willReturn("M");
            given(productDetail.getColor()).willReturn("BLACK");

            given(cartRepository.findByUser(any(User.class))).willReturn(List.of(cart));

            // when
            CartResponseDto cartResponseDto = cartService.getCart(user);

            // then
            List<CartItemDto> items = cartResponseDto.getCartItemDtoList();
            assertThat(items).isNotEmpty();
            assertThat(items.get(0).getProduct()).isEqualTo("꽃무늬 원피스");
            assertThat(cartResponseDto.getTotalPrice()).isEqualTo(100);

        }
    }
}