package com.supernova.fashionnova.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.supernova.fashionnova.config.QueryDslConfig;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * create on 2024/07/24 create by IntelliJ IDEA.
 *
 * <p> New Project. </p>
 *
 * @author HoChan Son (hcson)
 * @version 1.0
 * @since 1.0
 */
@Import(QueryDslConfig.class)
@DataJpaTest
class ProductRepositoryQueryImplTest {

  @Autowired
  private ProductRepository productRepository;


  @Test
  void findProductByOrdered() {

    Product p = new Product("product", 100,
        "ttt", "asd", "asd", 1, 1, "ACTIVE");

    productRepository.save(p);
    Pageable pa = PageRequest.of(0, 10);
    List<Product> products = productRepository.findProductByOrdered("high_price", null, pa);
    assertThat(products).isNotEmpty();
  }
}