package com.supernova.fashionnova.wish;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductCategory;
import com.supernova.fashionnova.product.ProductRepository;
import com.supernova.fashionnova.product.ProductStatus;
import com.supernova.fashionnova.product.dto.ProductResponseDto;
import com.supernova.fashionnova.user.User;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class WishServiceTest {

    @Mock
    private WishRepository wishRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private WishService wishService;

    @Test
    void addWishTest() {

        // given
        User user = Mockito.mock(User.class);
        Long productId = 1L;

        Product product = Mockito.mock(Product.class);
        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when
        assertDoesNotThrow(() -> wishService.addWish(user, productId));

        // then
        verify(wishRepository, times(1)).save(any(Wish.class));

    }

    @Test
    void getWishProductListTest() {

        // given
        User user = Mockito.mock(User.class);
        int page = 1;

        Wish wish = Mockito.mock(Wish.class);
        given(wish.getProduct()).willReturn(new Product("Test Product", 10000, "Test Explanation", ProductCategory.TOP, ProductStatus.ACTIVE));
        given(wishRepository.findByUser(user, PageRequest.of(page, 10)))
            .willReturn(new PageImpl<>(Collections.singletonList(wish)));

        // when
        Page<ProductResponseDto> result = wishService.getWishProductList(user, page);

        // then
        assertNotNull(result);
        assertEquals(1, result.getNumberOfElements());

    }

    @Test
    void deleteWishTest() {

        // given
        User user = Mockito.mock(User.class);
        Long wishId = 1L;

        Wish wish = Mockito.mock(Wish.class);
        given(wish.getUser()).willReturn(user);
        given(wishRepository.findById(wishId)).willReturn(Optional.of(wish));

        // when
        assertDoesNotThrow(() -> wishService.deleteWish(user, wishId));

        // then
        verify(wishRepository, times(1)).delete(any(Wish.class));

    }

}
