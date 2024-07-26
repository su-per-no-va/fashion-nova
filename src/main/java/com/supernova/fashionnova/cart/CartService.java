package com.supernova.fashionnova.cart;

import com.supernova.fashionnova.cart.dto.CartItemDto;
import com.supernova.fashionnova.cart.dto.CartRequestDto;
import com.supernova.fashionnova.cart.dto.CartResponseDto;
import com.supernova.fashionnova.cart.dto.CartUpdateRequestDto;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductDetail;
import com.supernova.fashionnova.product.ProductDetailRepository;
import com.supernova.fashionnova.product.ProductRepository;
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

    private final ProductRepository productRepository;

    /**
     * 장바구니에 상품 추가
     *
     * @param user 사용자 정보
     * @param dto 장바구니 데이터
     * @throws CustomException NOT_FOUND_PRODUCT 상품 정보를 찾을 수 없을 때
     * @throws CustomException OUT_OF_STOCK 품절된 상품일 때
     */
    @Transactional
    public void addCart(User user, CartRequestDto dto) {

        // 상품 조회
        Product product = productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));

        // 상품 디테일 조회
        ProductDetail productDetail = productDetailRepository.findByProductAndSizeAndColor(product,
                dto.getSize(), dto.getColor())
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));


        // 상품 재고 확인
        if (productDetail.getQuantity() == 0 || !"ACTIVE".equals(productDetail.getStatus().toString())) {
            throw new CustomException(ErrorType.OUT_OF_STOCK);
        }

        // 이미 담겨진 상품 장바구니가 있는지 조회
        Optional<Cart> result = cartRepository.findByUserAndProductDetail(user, productDetail);
        int count = dto.getCount();
        int price = product.getPrice();
        int totalPrice = count * price;

        if (result.isPresent()) {
            Cart cart = result.get();
            cart.incrementCount(count);
            cart.incrementTotalPrice(totalPrice);
        } else {
            Cart cart = new Cart(count, totalPrice, user, productDetail);
            cartRepository.save(cart);
        }
    }

    /**
     * 장바구니 조회
     *
     * @param user 사용자 정보
     * @return CartResponseDto
     */
    @Transactional(readOnly = true)
    public CartResponseDto getCart(User user) {

        List<Cart> cartList = cartRepository.findByUser(user);
        List<CartItemDto> cartItemDtoList = cartList.stream()
            .map(cart -> new CartItemDto(
                cart.getProductDetail().getProduct().getProduct(),
                cart.getProductDetail().getProduct().getPrice(),
                cart.getCount(),
                cart.getProductDetail().getSize(),
                cart.getProductDetail().getColor()))
            .toList();

        int totalPrice = cartList.stream().mapToInt(Cart::getTotalPrice).sum();

        return new CartResponseDto(cartItemDtoList, totalPrice);
    }

    /**
     * 장바구니 수정
     *
     * @param user 사용자 정보
     * @param dto 장바구니 데이터
     * @throws CustomException NOT_FOUND_PRODUCT 상품 정보를 찾을 수 없을 때
     * @throws CustomException OUT_OF_STOCK 품절된 상품일 때
     */
    @Transactional
    public void updateCart(User user, CartUpdateRequestDto dto) {

        ProductDetail currentProductDetail = productDetailRepository.findById(
                dto.getProductDetailId())
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));

        ProductDetail newProductDetail = productDetailRepository.findByProductAndSizeAndColor(
                currentProductDetail.getProduct(),
                dto.getSize(), dto.getColor())
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));

        if (newProductDetail.getQuantity() == 0) {
            throw new CustomException(ErrorType.OUT_OF_STOCK);
        }

        // 현재 장바구니 조회
        Cart cart = cartRepository.findByUserAndProductDetail(user, currentProductDetail)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));

        // 새로운 상품 상세 정보가 이미 장바구니에 있는지 확인
        Optional<Cart> existingCartOptional = cartRepository.findByUserAndProductDetail(user,
            newProductDetail);
        if (existingCartOptional.isPresent()) {
            // 이미 존재하면 수량 증가
            Cart existingCart = existingCartOptional.get();
            existingCart.incrementCount(cart.getCount());
            existingCart.incrementTotalPrice(cart.getTotalPrice());
            cartRepository.save(existingCart);

            // 기존 장바구니 항목 삭제
            cartRepository.delete(cart);
        } else {
            // 존재하지 않으면 새로 추가
            cart.setProductDetail(newProductDetail);
            cart.updateCountPrice(dto.getCount());
            cartRepository.save(cart);
        }
    }

    /**
     * 장바구니 상품 삭제
     *
     * @param user      사용자 정보
     * @param cartDeleteRequestDto
     * @throws CustomException NOT_FOUND_PRODUCT 상품을 찾을 수 없을 때
     */
    @Transactional
    public void deleteFromCart(User user, Long productDetailId) {
        Cart cart = cartRepository.findByUserAndProductDetail(user,
                productDetailRepository.findById(productDetailId)
                    .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PRODUCT)))
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));

        cartRepository.delete(cart);
    }

    /**
     * 장바구니 비우기
     *
     * @param user      사용자 정보
     * @throws CustomException CART_EMPTY 장바구니에 상품이 존재하지 않을 때
     */
    @Transactional
    public void clearCart(User user) {

        List<Cart> cartList = cartRepository.findByUser(user);

        if (cartList.isEmpty()) {
            throw new CustomException(ErrorType.CART_EMPTY);
        }

        cartRepository.deleteAllInBatch(cartList);

    }
}
