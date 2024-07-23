package com.supernova.fashionnova.wish;

import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductRepository;
import com.supernova.fashionnova.product.dto.ProductResponseDto;
import com.supernova.fashionnova.user.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishService {

    private final ProductRepository productRepository;
    private final WishRepository wishRepository;

    /** 위시리스트 추가
     *
     * @param user
     * @param productId
     * @throws CustomException NOT_FOUND_PRODUCT 상품을 찾지 못할 때
     */
    public void addWish(User user, Long productId) {

        Product product = getProduct(productId);

        Wish wish = Wish.builder()
            .user(user)
            .product(product)
            .build();

        wishRepository.save(wish);

    }

    /** 위시리스트 조회
     *
     * @param user
     * @param page
     */
    public Page<ProductResponseDto> getWishProductList(User user, int page) {
        Pageable pageable = PageRequest.of(page, 10);

        Page<Wish> wishPage = wishRepository.findByUser(user, pageable);

        List<ProductResponseDto> productResponseDtoList = wishPage.getContent().stream()
            .map(wish -> new ProductResponseDto(wish.getProduct()))
            .collect(Collectors.toList());

        return new PageImpl<>(productResponseDtoList, pageable, wishPage.getTotalElements());
    }

    /** 위시리스트 삭제
     *
     * @param user
     * @param productId
     * @throws CustomException NOT_FOUND_WISH 위시리스트를 찾지 못할 때
     * @throws CustomException INVALID_WISH 자신의 위시리스트가 아닐 때
     */
    public void deleteWish(User user, Long wishId) {

        Wish wish = getWish(wishId);
        validateUser(user, wish);

        wishRepository.delete(wish);
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
            () -> new CustomException(ErrorType.NOT_FOUND_PRODUCT)
        );
    }

    private Wish getWish(Long wishId) {
        return wishRepository.findById(wishId).orElseThrow(
            () -> new CustomException(ErrorType.NOT_FOUND_WISH)
        );
    }

    private void validateUser(User user, Wish wish) {
        if (!wish.getUser().equals(user)) {
            throw new CustomException(ErrorType.INVALID_WISH);
        }
    }

}
