package com.supernova.fashionnova.product;


import com.supernova.fashionnova.product.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductRepositoryQuery {
   Page<ProductResponseDto> findProductByOrdered(String sort, String category, String size, String color, String search, Pageable pageable);
}
