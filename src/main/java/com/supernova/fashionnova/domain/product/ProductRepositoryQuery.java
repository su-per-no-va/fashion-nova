package com.supernova.fashionnova.domain.product;

import com.supernova.fashionnova.domain.product.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryQuery {

   Page<ProductResponseDto> findProductByOrdered(String sort, String category, String size, String color, String search, Pageable pageable);

}
