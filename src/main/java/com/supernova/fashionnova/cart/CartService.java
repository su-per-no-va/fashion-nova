package com.supernova.fashionnova.cart;

import com.supernova.fashionnova.cart.dto.CartItemDto;
import com.supernova.fashionnova.cart.dto.CartResponseDto;
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
     * @throws CustomException NOT_FOUND_USER 상품 정보를 찾을 수 없을 때
     * @throws CustomException OUT_OF_STOCK 품절된 상품일 때
     */
    public void addCart(User user, Long productDetailId, int count) {
        Optional<ProductDetail> productDetailOptional = productDetailRepository.findById(productDetailId);

        if (productDetailOptional.isEmpty()) {
            throw new CustomException(ErrorType.NOT_FOUND_PRODUCTDETAIL); // 상품 정보를 찾을 수 없을 때
        }

        ProductDetail productDetail = productDetailOptional.get();

        if (productDetail.getQuantity() == 0) {
            throw new CustomException(ErrorType.OUT_OF_STOCK); // 품절된 상품일 때
        }

        Product product = productDetail.getProduct();
        int price = product.getPrice();

        // 사용자의 장바구니를 가져옵니다.
        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.assignUser(user);
            return newCart;
        });

        // 장바구니에 제품 상세 정보를 추가합니다.
        cart.getProductDetailList().add(productDetail);
        cart.incrementCount(count);
        cart.incrementTotalPrice(price * count);

        // 장바구니를 저장합니다.
        cartRepository.save(cart);
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

        List<CartItemDto> items = cart.getProductDetailList().stream()
            .map(detail -> new CartItemDto(
                detail.getProduct().getProduct(),
                detail.getProduct().getPrice(),
                cart.getCount(),
                detail.getSize(),
                detail.getColor()))
            .toList();

        return new CartResponseDto(items, cart.getTotalPrice());
    }
}
