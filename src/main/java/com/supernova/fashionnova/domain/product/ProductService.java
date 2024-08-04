package com.supernova.fashionnova.domain.product;

import com.supernova.fashionnova.domain.product.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 조건별 상품 검색
     * @param sorted
     * @param category
     * @param size
     * @param color
     * @param search
     * @param page
     * @return 페이징
     */
    public Page<ProductResponseDto> getProductList(String sorted, String category, String size, String color, String search, int page) {
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sorted);
        Pageable pageable = PageRequest.of(page, 10, sort);
        
        return productRepository.findProductByOrdered(sorted, category, size, color, search, pageable);
    }

}
