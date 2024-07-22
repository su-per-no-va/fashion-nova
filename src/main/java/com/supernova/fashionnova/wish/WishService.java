package com.supernova.fashionnova.wish;

import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductRepository;
import com.supernova.fashionnova.user.User;
import com.supernova.fashionnova.wish.dto.WishResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishService {

    private final ProductRepository productRepository;
    private final WishRepository wishRepository;

    /** 위시리스트 찜
     *
     * @param user
     * @param productId
     * @throws CustomException NOT_FOUND_PRODUCT 상품을 찾지 못할 때
     */
    @Transactional
    public boolean updateWish(User user, Long productId) {

        Product product = getProduct(productId);

        Wish wish = wishRepository.findByUserAndProduct(user, product)
            .orElseGet(() -> new Wish(user, product));

        wish.updateWish();

        return wish.isWish();
    }

    /** 위시리스트 조회
     *
     * @param user
     * @param page
     */
    public Page<WishResponseDto> getWishProductList(User user, int page) {
        Pageable pageable = PageRequest.of(page, 10);

        Page<Wish> wishPage = wishRepository.findByUser(user, pageable);

        List<WishResponseDto> wishResponseDtoList = wishPage.getContent().stream()
            .map(WishResponseDto::new)
            .collect(Collectors.toList());

        return new PageImpl<>(wishResponseDtoList, pageable, wishPage.getTotalElements());
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
            () -> new CustomException(ErrorType.NOT_FOUND_PRODUCT)
        );
    }
}
