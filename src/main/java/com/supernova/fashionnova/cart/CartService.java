package com.supernova.fashionnova.cart;

import com.supernova.fashionnova.cart.dto.CartRequestDto;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductDetail;
import com.supernova.fashionnova.product.ProductDetailRepository;
import com.supernova.fashionnova.product.ProductRepository;
import com.supernova.fashionnova.user.User;
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
        if (productDetail.getQuantity() == 0 || !"ACTIVE".equals(productDetail.getStatus())) {
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
//
//    /**
//     * 장바구니 조회
//     *
//     * @param user 사용자 정보
//     * @return CartResponseDto
//     */
//    @Transactional(readOnly = true)
//    public CartResponseDto getCart(User user) {
//        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> {
//            Cart newCart = new Cart();
//            newCart.assignUser(user);
//            return newCart;
//        });
//
//        List<CartItemDto> cartItemDtoList = cart.getProductDetailList().stream()
//            .map(detail -> new CartItemDto(
//                detail.getProduct().getProduct(),
//                detail.getProduct().getPrice(),
//                cart.getCount(),
//                detail.getSize(),
//                detail.getColor()))
//            .toList();
//
//        return new CartResponseDto(cartItemDtoList, cart.getTotalPrice());
//    }
//
//    /**
//     * 장바구니 수정
//     *
//     * @param user 사용자 정보
//     * @param dto
//     * @throws CustomException NOT_FOUND_PRODUCT 상품 정보를 찾을 수 없을 때
//     * @throws CustomException OUT_OF_STOCK 품절된 상품일 때
//     */
//    @Transactional
//    public void updateCart(User user, CartRequestDto dto) {
//        Cart cart = cartRepository.findByUserId(user.getId())
//            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));
//
//        ProductDetail currentProductDetail = cart.getProductDetailList().stream()
//            .filter(detail -> detail.getProduct().getId().equals(dto.getProductId()))
//            .findFirst()
//            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));
//
//        // 새로운 사이즈와 색상에 맞는 ProductDetail 찾기
//        ProductDetail newProductDetail = productDetailRepository.findByProductAndSizeAndColor(
//            currentProductDetail.getProduct(),
//            dto.getSize(),
//            dto.getColor()
//        ).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));
//
//        if (newProductDetail.getQuantity() == 0) {
//            throw new CustomException(ErrorType.OUT_OF_STOCK);
//        }
//
//        // 새로운 ProductDetail로 교체
//        cart.getProductDetailList().remove(currentProductDetail);
//        cart.getProductDetailList().add(newProductDetail);
//
//        // 상품 수량이 수정된 경우 반영
//        if (dto.getCount() != null) {
//            int oldCount = cart.getCount();
//            int priceDifference = (dto.getCount() - oldCount) * newProductDetail.getProduct().getPrice();
//            cart.setCount(dto.getCount());
//            cart.incrementTotalPrice(priceDifference);
//        }
//
//    }
//
//    /**
//     * 장바구니 상품 삭제
//     *
//     * @param user      사용자 정보
//     * @param cartDeleteRequestDto
//     * @throws CustomException NOT_FOUND_PRODUCT 상품 정보를 찾을 수 없을 때
//     */
//    @Transactional
//    public void deleteFromCart(User user, Long productDetailId) {
//        Cart cart = cartRepository.findByUserId(user.getId())
//            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));
//
//        ProductDetail productDetail = cart.getProductDetailList().stream()
//            .filter(detail -> detail.getId().equals(productDetailId))
//            .findFirst()
//            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));
//
//        cart.getProductDetailList().remove(productDetail);
//
//    }
//
//    /**
//     * 장바구니 비우기
//     *
//     * @param user      사용자 정보
//     * @throws CustomException CART_EMPTY 장바구니에 상품이 존재하지 않을 때
//     */
//    @Transactional
//    public void clearCart(User user) {
//        Cart cart = cartRepository.findByUserId(user.getId())
//            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));
//
//        if (cart.getProductDetailList().isEmpty()) {
//            throw new CustomException(ErrorType.CART_EMPTY);
//        }
//
//        cart.getProductDetailList().clear();
//
//    }
}
