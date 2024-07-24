package com.supernova.fashionnova.cart;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.supernova.fashionnova.cart.dto.CartRequestDto;
import com.supernova.fashionnova.cart.dto.CartUpdateRequestDto;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductDetail;
import com.supernova.fashionnova.product.ProductDetailRepository;
import com.supernova.fashionnova.product.ProductRepository;
import com.supernova.fashionnova.user.User;
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
                "black"
            );

            Product product = mock(Product.class);
            ProductDetail productDetail = mock(ProductDetail.class);

            given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
            given(productDetailRepository.findByProductAndSizeAndColor(any(Product.class), anyString(), anyString())).willReturn(Optional.of(productDetail));
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
                "black"
            );

            Product product = mock(Product.class);
            ProductDetail productDetail = mock(ProductDetail.class);

            given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
            given(productDetailRepository.findByProductAndSizeAndColor(any(Product.class), anyString(), anyString())).willReturn(Optional.of(productDetail));
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

        }
    }

    @Nested
    class UpdateCartTest {

        @Test
        @DisplayName("장바구니 수정 성공 테스트")
        void UpdateCartTest1() {
            // given
            User user = mock(User.class);
            CartUpdateRequestDto requestDto = new CartUpdateRequestDto(1L, 2, "L", "blue");

            Product product = mock(Product.class);
            ProductDetail currentProductDetail = mock(ProductDetail.class);
            ProductDetail newProductDetail = mock(ProductDetail.class);
            Cart cart = new Cart(1, 100, user, currentProductDetail);

            given(productDetailRepository.findById(anyLong())).willReturn(Optional.of(currentProductDetail));
            given(productDetailRepository.findByProductAndSizeAndColor(any(Product.class), anyString(), anyString())).willReturn(Optional.of(newProductDetail));
            given(newProductDetail.getQuantity()).willReturn(10L);
            given(cartRepository.findByUserAndProductDetail(any(User.class), any(ProductDetail.class))).willReturn(Optional.of(cart));
            given(cartRepository.findByUserAndProductDetail(any(User.class), eq(newProductDetail))).willReturn(Optional.empty());

            // when
            assertDoesNotThrow(() -> cartService.updateCart(user, requestDto));

            // then
            verify(cartRepository).save(any(Cart.class));
        }

        @Test
        @DisplayName("장바구니 수정 실패 테스트 - 상품 정보 없음")
        void UpdateCartTest2() {
            // given
            User user = mock(User.class);
            CartUpdateRequestDto requestDto = new CartUpdateRequestDto(1L, 2, "L", "blue");

            given(productDetailRepository.findById(anyLong())).willReturn(Optional.empty());

            // when / then
            assertThrows(CustomException.class, () -> cartService.updateCart(user, requestDto));
        }

        @Test
        @DisplayName("장바구니 수정 실패 테스트 - 품절된 상품")
        void UpdateCartTest3() {
            // given
            User user = mock(User.class);
            CartUpdateRequestDto requestDto = new CartUpdateRequestDto(1L, 2, "L", "blue");

            Product product = mock(Product.class);
            ProductDetail currentProductDetail = mock(ProductDetail.class);
            ProductDetail newProductDetail = mock(ProductDetail.class);

            given(productDetailRepository.findById(anyLong())).willReturn(Optional.of(currentProductDetail));
            given(productDetailRepository.findByProductAndSizeAndColor(any(Product.class), anyString(), anyString())).willReturn(Optional.of(newProductDetail));
            given(newProductDetail.getQuantity()).willReturn(0L);

            // when / then
            assertThrows(CustomException.class, () -> cartService.updateCart(user, requestDto));
        }
    }

    @Test
    void deleteFromCartTest() {
    }

    @Test
    void clearCartTest() {
    }
}