package com.supernova.fashionnova.cart;

import com.supernova.fashionnova.cart.dto.CartItemDto;
import com.supernova.fashionnova.cart.dto.CartResponseDto;
import com.supernova.fashionnova.cart.dto.CartUpdateRequestDto;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductDetail;
import com.supernova.fashionnova.product.ProductDetailRepository;
import com.supernova.fashionnova.user.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final ProductDetailRepository productDetailRepository;

    /**
     * 장바구니에 상품 추가
     *
     * @param user 사용자 정보
     * @param productDetailId 상품 상세 ID
     * @param count 상품 수량
     * @throws CustomException NOT_FOUND_PRODUCT 상품 정보를 찾을 수 없을 때
     * @throws CustomException OUT_OF_STOCK 품절된 상품일 때
     */
    public void addCart(User user, Long productDetailId, int count) {

        Optional<ProductDetail> productDetailOptional = productDetailRepository.findById(productDetailId);

        if (productDetailOptional.isEmpty()) {
            throw new CustomException(ErrorType.NOT_FOUND_PRODUCT);
        }

            ProductDetail productDetail = productDetailOptional.get();

            if (productDetail.getQuantity() == 0 || !"ACTIVE".equals(productDetail.getStatus())) {
                throw new CustomException(ErrorType.OUT_OF_STOCK);
            }

            Product product = productDetail.getProduct();
            int price = product.getPrice();

            // 사용자의 장바구니를 가져옵니다.
            Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> {
                Cart newCart = new Cart();
                newCart.assignUser(user);
                return newCart;
            });

            cart.getProductDetailList().add(productDetail);
            cart.incrementCount(count);
            cart.incrementTotalPrice(price * count);

        }


    /**
     * 장바구니 조회
     *
     * @param user 사용자 정보
     * @return CartResponseDto
     */
    @Transactional(readOnly = true)
    public CartResponseDto getCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.assignUser(user);
            return newCart;
        });

        List<CartItemDto> cartItemDtoList = cart.getProductDetailList().stream()
            .map(detail -> new CartItemDto(
                detail.getProduct().getProduct(),
                detail.getProduct().getPrice(),
                cart.getCount(),
                detail.getSize(),
                detail.getColor()))
            .toList();

        return new CartResponseDto(cartItemDtoList, cart.getTotalPrice());
    }

    /**
     * 장바구니 수정
     *
     * @param user 사용자 정보
     * @param cartUpdateRequestDto
     * @throws CustomException NOT_FOUND_PRODUCT 상품 정보를 찾을 수 없을 때
     * @throws CustomException OUT_OF_STOCK 품절된 상품일 때
     */
    @Transactional
    public void updateCart(User user, CartUpdateRequestDto cartUpdateRequestDto) {
        Cart cart = cartRepository.findByUserId(user.getId())
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));

        ProductDetail currentProductDetail = cart.getProductDetailList().stream()
            .filter(detail -> detail.getProduct().getId().equals(cartUpdateRequestDto.getProductId()))
            .findFirst()
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));

        // 새로운 사이즈와 색상에 맞는 ProductDetail 찾기
        ProductDetail newProductDetail = productDetailRepository.findByProductAndSizeAndColor(
            currentProductDetail.getProduct(),
            cartUpdateRequestDto.getSize(),
            cartUpdateRequestDto.getColor()
        ).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));

        if (newProductDetail.getQuantity() == 0) {
            throw new CustomException(ErrorType.OUT_OF_STOCK);
        }

        // 새로운 ProductDetail로 교체
        cart.getProductDetailList().remove(currentProductDetail);
        cart.getProductDetailList().add(newProductDetail);

        // 상품 수량이 수정된 경우 반영
        if (cartUpdateRequestDto.getCount() != null) {
            int oldCount = cart.getCount();
            int priceDifference = (cartUpdateRequestDto.getCount() - oldCount) * newProductDetail.getProduct().getPrice();
            cart.setCount(cartUpdateRequestDto.getCount());
            cart.incrementTotalPrice(priceDifference);
        }

    }

    /**
     * 장바구니 상품 삭제
     *
     * @param user      사용자 정보
     * @param cartDeleteRequestDto
     * @throws CustomException NOT_FOUND_PRODUCT 상품 정보를 찾을 수 없을 때
     */
    @Transactional
    public void deleteFromCart(User user, Long productDetailId) {
        Cart cart = cartRepository.findByUserId(user.getId())
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));

        ProductDetail productDetail = cart.getProductDetailList().stream()
            .filter(detail -> detail.getId().equals(productDetailId))
            .findFirst()
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));

        cart.getProductDetailList().remove(productDetail);

    }

    /**
     * 장바구니 비우기
     *
     * @param user      사용자 정보
     * @throws CustomException CART_EMPTY 장바구니에 상품이 존재하지 않을 때
     */
    @Transactional
    public void clearCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId())
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));

        if (cart.getProductDetailList().isEmpty()) {
            throw new CustomException(ErrorType.CART_EMPTY);
        }

        cart.getProductDetailList().clear();

    }
}
