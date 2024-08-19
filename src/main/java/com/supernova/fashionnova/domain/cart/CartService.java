package com.supernova.fashionnova.domain.cart;

import com.supernova.fashionnova.domain.cart.dto.CartItemDto;
import com.supernova.fashionnova.domain.cart.dto.CartRequestDto;
import com.supernova.fashionnova.domain.cart.dto.CartResponseDto;
import com.supernova.fashionnova.domain.cart.dto.CartUpdateRequestDto;
import com.supernova.fashionnova.domain.product.Product;
import com.supernova.fashionnova.domain.product.ProductDetail;
import com.supernova.fashionnova.domain.product.ProductDetailRepository;
import com.supernova.fashionnova.domain.product.ProductRepository;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;

    /**
     * 장바구니에 상품 추가
     *
     * @param user 사용자 정보
     * @param dto  장바구니 데이터
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
        if (productDetail.getQuantity() == 0 || !"ACTIVE".equals(
            productDetail.getStatus().toString())) {
            throw new CustomException(ErrorType.OUT_OF_STOCK);
        }

        // 이미 담겨진 상품 장바구니가 있는지 조회
        Optional<Cart> result = cartRepository.findByUserAndProductDetail(user, productDetail);
        int count = dto.getCount();
        Long price = product.getPrice();
        Long totalPrice = count * price;

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
                cart.getProductDetail().getColor(),
                cart.getProductDetail().getProduct().getImageUrl(),
                cart.getProductDetail().getId(),
                cart.getProductDetail().getProduct().getProductDetailList().stream()
                    .map(ProductDetail::getColor).toList(),
                cart.getProductDetail().getProduct().getProductDetailList().stream()
                    .map(ProductDetail::getSize).toList()
            ))
            .toList();

        Long totalPrice = cartList.stream().mapToLong(Cart::getTotalPrice).sum();

        return new CartResponseDto(cartItemDtoList, totalPrice);
    }

    /**
     * 장바구니 수정
     *
     * @param user 사용자 정보
     * @param dto  장바구니 데이터
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
            .orElse(currentProductDetail);

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

            if (!cart.equals(existingCart)) {
                existingCart.incrementCount(cart.getCount());
                existingCart.incrementTotalPrice(cart.getTotalPrice());

                cartRepository.delete(cart);
            }else{
                existingCart.updateCountPrice(dto.getCount());
            }
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
     * @param user            사용자 정보
     * @param productDetailId
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
     * @param userId 사용자 정보
     * @throws CustomException CART_EMPTY 장바구니에 상품이 존재하지 않을 때
     */
    @Transactional
    public void clearCart(Long userId) {

        List<Cart> cartList = cartRepository.findAllByUserId(userId);

        if (cartList.isEmpty()) {
            throw new CustomException(ErrorType.CART_EMPTY);
        }

        cartRepository.deleteAllInBatch(cartList);
    }

}
