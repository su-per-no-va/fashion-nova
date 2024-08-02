package com.supernova.fashionnova.product;

import com.supernova.fashionnova.global.config.JpaAuditingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * create on 2024/07/24 create by IntelliJ IDEA.
 *
 * <p> New Project. </p>
 *
 * @author HoChan Son (hcson)
 * @version 1.0
 * @since 1.0
 */
@Import(JpaAuditingConfig.class)
@DataJpaTest
class ProductRepositoryQueryImplTest {

    @Autowired
    private ProductRepository productRepository;

    /*
    @Test
    @DisplayName("높은 가격순")
    void test1() {

        Product p1 = new Product("Test Product", 1000L, "Test Explanation", ProductCategory.TOP,
            ProductStatus.ACTIVE);
        Product p2 = new Product("Test Product", 3000L, "Test Explanation", ProductCategory.TOP,
            ProductStatus.ACTIVE);
        Product p3 = new Product("Test Product", 5000L, "Test Explanation", ProductCategory.TOP,
            ProductStatus.ACTIVE);

        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
        Pageable pa = PageRequest.of(0, 10);
        Page<ProductResponseDto> products = productRepository.findProductByOrdered("high_price",
            null, null, null, pa);
        assertThat(products).isNotEmpty();
        assertEquals(5000, products.getContent().get(0).getPrice());
        assertEquals(3000, products.getContent().get(1).getPrice());
        assertEquals(1000, products.getContent().get(2).getPrice());
    }

    @Test
    @DisplayName("낮은 가격순")
    void test2() {

        Product p1 = new Product("Test Product", 1000L, "Test Explanation", ProductCategory.TOP,
            ProductStatus.ACTIVE);
        Product p2 = new Product("Test Product", 3000L, "Test Explanation", ProductCategory.TOP,
            ProductStatus.ACTIVE);
        Product p3 = new Product("Test Product", 5000L, "Test Explanation", ProductCategory.TOP,
            ProductStatus.ACTIVE);

        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
        Pageable pa = PageRequest.of(0, 10);
        Page<ProductResponseDto> products = productRepository.findProductByOrdered("row_price",
            null, null, null, pa);
        assertThat(products).isNotEmpty();
        assertEquals(1000, products.getContent().get(0).getPrice());
        assertEquals(3000, products.getContent().get(1).getPrice());
        assertEquals(5000, products.getContent().get(2).getPrice());
    }

    @Test
    @DisplayName("신상품순")
    void test3() {

        Product p1 = new Product("first item", 1000L, "Test Explanation", ProductCategory.TOP,
            ProductStatus.ACTIVE);
        Product p2 = new Product("second item", 3000L, "Test Explanation", ProductCategory.TOP,
            ProductStatus.ACTIVE);
        Product p3 = new Product("third item", 5000L, "Test Explanation", ProductCategory.TOP,
            ProductStatus.ACTIVE);

        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
        Pageable pa = PageRequest.of(0, 10);
        Page<ProductResponseDto> products = productRepository.findProductByOrdered("new_item", null,
            null, null, pa);
        assertThat(products).isNotEmpty();
        assertEquals("third item", products.getContent().get(0).getProduct());
        assertEquals("second item", products.getContent().get(1).getProduct());
        assertEquals("first item", products.getContent().get(2).getProduct());
    }
    @Test
    @DisplayName("리뷰많은 순")
    void test4() {

        Product p1 = new Product("first item", 1000, "Test Explanation", ProductCategory.TOP,
            ProductStatus.ACTIVE);
        Product p2 = new Product("second item", 3000, "Test Explanation", ProductCategory.TOP,
            ProductStatus.ACTIVE);
        Product p3 = new Product("third item", 5000, "Test Explanation", ProductCategory.TOP,
            ProductStatus.ACTIVE);

        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
        Pageable pa = PageRequest.of(0, 10);
        Page<ProductResponseDto> products = productRepository.findProductByOrdered("new_item", null,
            null, null, pa);
        assertThat(products).isNotEmpty();
        assertEquals("third item", products.getContent().get(0).getProduct());
        assertEquals("second item", products.getContent().get(1).getProduct());
        assertEquals("first item", products.getContent().get(2).getProduct());
    }
    @Test
    @DisplayName("좋아요 순")
    void test5() {

        Product p1 = new Product("first item", 1000, "Test Explanation", ProductCategory.TOP,
            ProductStatus.ACTIVE);
        Product p2 = new Product("second item", 3000, "Test Explanation", ProductCategory.TOP,
            ProductStatus.ACTIVE);
        Product p3 = new Product("third item", 5000, "Test Explanation", ProductCategory.TOP,
            ProductStatus.ACTIVE);

        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
        Pageable pa = PageRequest.of(0, 10);
        Page<ProductResponseDto> products = productRepository.findProductByOrdered("new_item", null,
            null, null, pa);
        assertThat(products).isNotEmpty();
        assertEquals("third item", products.getContent().get(0).getProduct());
        assertEquals("second item", products.getContent().get(1).getProduct());
        assertEquals("first item", products.getContent().get(2).getProduct());
    }

    @Test
    @DisplayName("카테고리 + 신상품순")
    void test6() {

        Product p1 = new Product("first item", 1000L, "Test Explanation", ProductCategory.TOP,
            ProductStatus.ACTIVE);
        Product p2 = new Product("second item", 3000L, "Test Explanation", ProductCategory.BOTTOM,
            ProductStatus.ACTIVE);
        Product p3 = new Product("third item", 5000L, "Test Explanation", ProductCategory.TOP,
            ProductStatus.ACTIVE);

        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
        Pageable pa = PageRequest.of(0, 10);
        Page<ProductResponseDto> products = productRepository.findProductByOrdered("new_item", "TOP",
            null, null, pa);
        assertThat(products).isNotEmpty();
        assertEquals(2, products.getTotalElements());
    }*/

}
