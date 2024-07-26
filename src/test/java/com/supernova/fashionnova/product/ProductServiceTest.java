package com.supernova.fashionnova.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

import com.supernova.fashionnova.product.dto.ProductResponseDto;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
@Mock
    private ProductRepository productRepository;
@InjectMocks
    private ProductService productService;


    @Test
    @DisplayName("상품 조회 테스트")
    void high_priceTest() {
        //given
        Product product1 = new Product("Test Product", 1000, "Test Explanation", ProductCategory.TOP, ProductStatus.ACTIVE);
        Product product2 = new Product("Test Product", 3000, "Test Explanation", ProductCategory.TOP, ProductStatus.ACTIVE);
        Product product3 = new Product("Test Product", 5000, "Test Explanation", ProductCategory.TOP, ProductStatus.ACTIVE);
        List<Product> productList = Arrays.asList(product1, product2, product3);

        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "high_price");
        Pageable pageable = PageRequest.of(1, 10, sort);

        List<ProductResponseDto> productDtoList = productList.stream()
            .map(ProductResponseDto::new)
            .collect(Collectors.toList());
        Page<ProductResponseDto> pageDto = new PageImpl<>(productDtoList, pageable,
            productDtoList.size());
        given(productRepository.findProductByOrdered("high_price", null, null, null,
            pageable)).willReturn(pageDto);

        //when
        Page<ProductResponseDto> result = productService.getProductList(1, null, null, null,
            "high_price");

        //then
        assertNotNull(result);
        assertEquals(1000, result.getContent().get(0).getPrice());
        assertEquals(3000, result.getContent().get(1).getPrice());
        assertEquals(5000, result.getContent().get(2).getPrice());
    }

}
