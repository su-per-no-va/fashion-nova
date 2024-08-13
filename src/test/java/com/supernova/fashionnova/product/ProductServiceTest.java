package com.supernova.fashionnova.product;

import com.supernova.fashionnova.domain.product.ProductRepository;
import com.supernova.fashionnova.domain.product.ProductService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    /*
    @Test
    @DisplayName("상품 조회 테스트")
    void high_priceTest() {
        //given
        Product product1 =
            new Product("Test Product", 1000L, "Test Explanation", ProductCategory.TOP, ProductStatus.ACTIVE);
        Product product2 =
            new Product("Test Product", 3000L, "Test Explanation", ProductCategory.TOP, ProductStatus.ACTIVE);
        Product product3 =
            new Product("Test Product", 5000L, "Test Explanation", ProductCategory.TOP, ProductStatus.ACTIVE);
        List<Product> productList = Arrays.asList(product1, product2, product3);

        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "high_price");
        Pageable pageable = PageRequest.of(1, 10, sort);

        List<ProductResponseDto> productDtoList = productList.stream()
            .map(ProductResponseDto::new)
            .collect(Collectors.toList());
        Page<ProductResponseDto> pageDto = new PageImpl<>(productDtoList, pageable, productDtoList.size());
        given(productRepository.findProductByOrdered("high_price", null, null, null, null, pageable))
            .willReturn(pageDto);

        //when
        Page<ProductResponseDto> result =
            productService.getProductList("high_price", null, null, null, null, 1);

        //then
        assertNotNull(result);
        assertEquals(1000L, result.getContent().get(0).getPrice());
        assertEquals(3000L, result.getContent().get(1).getPrice());
        assertEquals(5000L, result.getContent().get(2).getPrice());
    }
     */

}
