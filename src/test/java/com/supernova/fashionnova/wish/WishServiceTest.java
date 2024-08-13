package com.supernova.fashionnova.wish;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.supernova.fashionnova.domain.product.Product;
import com.supernova.fashionnova.domain.product.ProductRepository;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.domain.wish.Wish;
import com.supernova.fashionnova.domain.wish.WishRepository;
import com.supernova.fashionnova.domain.wish.WishService;
import com.supernova.fashionnova.domain.wish.dto.WishDeleteRequestDto;
import com.supernova.fashionnova.domain.wish.dto.WishRequestDto;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WishServiceTest {

    @Mock
    private WishRepository wishRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private WishService wishService;

    @Test
    @DisplayName("위시리스트 추가 테스트")
    void addWishTest() {

        // given
        User user = Mockito.mock(User.class);
        WishRequestDto requestDto = new WishRequestDto(1L);

        Product product = Mockito.mock(Product.class);
        given(productRepository.findById(requestDto.getProductId()))
            .willReturn(Optional.of(product));

        // when
        assertDoesNotThrow(() -> wishService.addWish(user, requestDto));

        // then
        verify(wishRepository, times(1)).save(any(Wish.class));

    }

    /*
    @Test
    @DisplayName("위시리스트 조회 테스트")
    void getWishProductPageTest() {

        // given
        User user = Mockito.mock(User.class);
        int page = 1;

        Wish wish = Mockito.mock(Wish.class);
        given(wish.getProduct()).willReturn(
            new Product("Test Product", 10000L, "Test Explanation", ProductCategory.TOP, ProductStatus.ACTIVE));
        given(wishRepository.findByUser(user, PageRequest.of(page, 10)))
            .willReturn(new PageImpl<>(Collections.singletonList(wish)));

        // when
        List<WishResponseDto> result = wishService.getWishProductList(user, page);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
    }
     */

    @Test
    @DisplayName("위시리스트 삭제 테스트")
    void deleteWishTest() {

        // given
        User user = Mockito.mock(User.class);
        WishDeleteRequestDto requestDto = new WishDeleteRequestDto(1L);

        Wish wish = Mockito.mock(Wish.class);
        Product product = Mockito.mock(Product.class);

        given(wish.getUser()).willReturn(user);
        given(wish.getProduct()).willReturn(product);
        given(wishRepository.findById(requestDto.getWishId())).willReturn(Optional.of(wish));

        // when
        assertDoesNotThrow(() -> wishService.deleteWish(user, requestDto));

        // then
        verify(wishRepository, times(1)).delete(any(Wish.class));
        verify(product, times(1)).decreaseWish();
    }

}
