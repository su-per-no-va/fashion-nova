package com.supernova.fashionnova.product;


import java.util.List;
import org.springframework.data.domain.Pageable;


public interface ProductRepositoryQuery {
   List<Product> findProductByOrdered(String sort, String category, Pageable pageable);
}
