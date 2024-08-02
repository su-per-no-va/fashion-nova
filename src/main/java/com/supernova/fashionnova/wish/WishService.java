package com.supernova.fashionnova.wish;

import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductRepository;
import com.supernova.fashionnova.product.dto.ProductResponseDto;
import com.supernova.fashionnova.user.User;
import com.supernova.fashionnova.wish.dto.WishDeleteRequestDto;
import com.supernova.fashionnova.wish.dto.WishRequestDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishService {

    private final ProductRepository productRepository;
    private final WishRepository wishRepository;

    /**
     * 위시리스트 추가
     *
     * @param user
     * @param requestDto
     * @throws CustomException NOT_FOUND_PRODUCT 상품을 찾지 못할 때
     */
    @Transactional
    public void addWish(User user, WishRequestDto requestDto) {

        Product product = getProduct(requestDto.getProductId());

        existsWish(user, product);

        Wish wish = Wish.builder()
            .user(user)
            .product(product)
            .build();

        wishRepository.save(wish);

        product.increaseWish();

    }

    /**
     * 위시리스트 조회
     *
     * @param user
     * @param page
     * @return List<ProductResponseDto>
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getWishProductList(User user, int page) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Wish> wishPage = wishRepository.findByUser(user, pageable);

        return wishPage.getContent().stream()
            .map(wish -> new ProductResponseDto(wish.getProduct()))
            .collect(Collectors.toList());

    }

    /**
     * 위시리스트 삭제
     *
     * @param user
     * @param requestDto
     * @throws CustomException NOT_FOUND_WISH 위시리스트를 찾지 못할 때
     * @throws CustomException INVALID_WISH 자신의 위시리스트가 아닐 때
     */
    @Transactional
    public void deleteWish(User user, WishDeleteRequestDto requestDto) {

        Wish wish = getWish(requestDto.getWishId());
        validateUser(user, wish);

        wishRepository.delete(wish);

        wish.getProduct().decreaseWish();

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
        if (!wish.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.INVALID_WISH);
        }
    }

    private void existsWish(User user, Product product) {
        boolean existsWish = wishRepository.existsByUserAndProduct(user, product);
        if (existsWish) {
            throw new CustomException(ErrorType.BAD_REQUEST_WISH_EXISTS);
        }
    }

}
