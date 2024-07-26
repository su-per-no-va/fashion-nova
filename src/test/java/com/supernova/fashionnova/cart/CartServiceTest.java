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
import com.supernova.fashionnova.product.ProductStatus;
import com.supernova.fashionnova.user.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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

    private User user;
    @BeforeEach
    void setUp() {
        this.user = User.builder()
            .userName("testUser1234")
            .name("테스트유저")
            .password("test1234!#")
            .email("test@gmail.com")
            .phone("010-1234-5678")
            .build();
    }

    @Nested
    class AddCartTest {

        @Test
        @DisplayName("장바구니 상품 추가 성공 테스트")
        void AddCartTest1() {
            // given
            CartRequestDto requestDto = new CartRequestDto(
                1L,
                2,
                "M",
                "BLACK"
            );

            Product product = mock(Product.class);
            ProductDetail productDetail = mock(ProductDetail.class);

            given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
            given(productDetailRepository.findByProductAndSizeAndColor(any(Product.class), anyString(), anyString())).willReturn(Optional.of(productDetail));
            given(productDetail.getQuantity()).willReturn(10L);
            given(productDetail.getStatus()).willReturn(ProductStatus.ACTIVE);

            // when
            assertDoesNotThrow(() -> cartService.addCart(user, requestDto));

            // then
            verify(cartRepository).save(any(Cart.class));
        }

        @Test
        @DisplayName("장바구니 상품 추가 실패 테스트 - 상품 정보 없음")
        void AddCartTest2() {
            // given
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
            CartRequestDto requestDto = new CartRequestDto(
                1L,
                2,
                "M",
                "BLACK"
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
            // given
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

    @Nested
    class UpdateCartTest {

        @Test
        @DisplayName("장바구니 수정 성공 테스트")
        void UpdateCartTest1() {
            // given
            CartUpdateRequestDto requestDto = new CartUpdateRequestDto(
                1L,
                2,
                "L",
                "BLACK");

            Product product = mock(Product.class);
            ProductDetail currentProductDetail = mock(ProductDetail.class);
            ProductDetail newProductDetail = mock(ProductDetail.class);
            Cart cart = new Cart(1, 100, user, currentProductDetail);

            given(currentProductDetail.getProduct()).willReturn(product);
            given(newProductDetail.getProduct()).willReturn(product);
            given(product.getPrice()).willReturn(5000); // 상품의 가격 설정

            given(currentProductDetail.getProduct()).willReturn(product);
            given(productDetailRepository.findById(anyLong())).willReturn(Optional.of(currentProductDetail));
            given(productDetailRepository.findByProductAndSizeAndColor(any(Product.class), anyString(), anyString())).willReturn(Optional.of(newProductDetail));
            given(newProductDetail.getQuantity()).willReturn(10L);
            given(cartRepository.findByUserAndProductDetail(any(User.class), eq(currentProductDetail))).willReturn(Optional.of(cart));
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
            CartUpdateRequestDto requestDto = new CartUpdateRequestDto(
                1L,
                2,
                "L",
                "BLACK");

            given(productDetailRepository.findById(anyLong())).willReturn(Optional.empty());

            // when / then
            assertThrows(CustomException.class, () -> cartService.updateCart(user, requestDto));
        }

        @Test
        @DisplayName("장바구니 수정 실패 테스트 - 품절된 상품")
        void UpdateCartTest3() {
            // given
            CartUpdateRequestDto requestDto = new CartUpdateRequestDto(
                1L,
                2,
                "L",
                "BLACK");

            Product product = mock(Product.class);
            ProductDetail currentProductDetail = mock(ProductDetail.class);
            ProductDetail newProductDetail = mock(ProductDetail.class);

            given(currentProductDetail.getProduct()).willReturn(product);
            given(productDetailRepository.findById(anyLong())).willReturn(Optional.of(currentProductDetail));
            given(productDetailRepository.findByProductAndSizeAndColor(any(Product.class), anyString(), anyString())).willReturn(Optional.of(newProductDetail));
            given(newProductDetail.getQuantity()).willReturn(0L);

            // when / then
            assertThrows(CustomException.class, () -> cartService.updateCart(user, requestDto));
        }
    }

    @Nested
    class deleteFromCartTest {

        @Test
        @DisplayName("장바구니 상품 삭제 성공 테스트")
        void deleteFromCartTest1() {
            // given
            Long productDetailId = 1L;
            ProductDetail productDetail = mock(ProductDetail.class);
            Cart cart = new Cart(1, 100, user, productDetail);

            given(productDetailRepository.findById(anyLong())).willReturn(Optional.of(productDetail));
            given(cartRepository.findByUserAndProductDetail(any(User.class), any(ProductDetail.class))).willReturn(Optional.of(cart));

            // when
            assertDoesNotThrow(() -> cartService.deleteFromCart(user, productDetailId));

            // then
            verify(cartRepository).delete(any(Cart.class));
        }

        @Test
        @DisplayName("장바구니 상품 삭제 실패 테스트 - 상품 정보 없음")
        void DeleteFromCartTest2() {
            // given
            Long productDetailId = 1L;

            given(productDetailRepository.findById(anyLong())).willReturn(Optional.empty());

            // when / then
            assertThrows(CustomException.class, () -> cartService.deleteFromCart(user, productDetailId));
        }

        @Test
        @DisplayName("장바구니 상품 삭제 실패 테스트 - 장바구니에 해당 상품 없음")
        void DeleteFromCartTest3() {
            // given
            Long productDetailId = 1L;
            ProductDetail productDetail = mock(ProductDetail.class);

            given(productDetailRepository.findById(anyLong())).willReturn(Optional.of(productDetail));
            given(cartRepository.findByUserAndProductDetail(any(User.class), any(ProductDetail.class))).willReturn(Optional.empty());

            // when / then
            assertThrows(CustomException.class, () -> cartService.deleteFromCart(user, productDetailId));
        }
    }

    @Nested
    class ClearCartTest {

        @Test
        @DisplayName("장바구니 비우기 성공 테스트")
        void ClearCartTest1() {
            // given
            ProductDetail productDetail = mock(ProductDetail.class);
            Cart cart1 = new Cart(1, 100, user, productDetail);
            Cart cart2 = new Cart(2, 200, user, productDetail);
            List<Cart> cartList = List.of(cart1, cart2); // mock Cart 리스트 생성

            given(cartRepository.findByUser(any(User.class))).willReturn(cartList);

            // when
            assertDoesNotThrow(() -> cartService.clearCart(user));

            // then
            verify(cartRepository).deleteAllInBatch(cartList);
        }

        @Test
        @DisplayName("장바구니 비우기 실패 테스트 - 장바구니가 이미 비어 있음")
        void ClearCartTest2() {
            // given
            given(cartRepository.findByUser(any(User.class))).willReturn(List.of());

            // when / then
            assertThrows(CustomException.class, () -> cartService.clearCart(user));
        }
    }
}