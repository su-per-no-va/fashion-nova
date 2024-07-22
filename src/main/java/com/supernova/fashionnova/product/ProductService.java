package com.supernova.fashionnova.product;

import com.supernova.fashionnova.product.dto.ProductResponseDto;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /** 조건별 상품 검색
     * @param sorted
     * @param category
     * @param page
     * @return 페이징
     */
    public Page<ProductResponseDto> getProductList(int page, String category, String sorted) {
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sorted);
        Pageable pageable = PageRequest.of(page, 10, sort);
        return new PageImpl<>(productRepository.findProductByOrdered(sorted, category, pageable).stream().map(ProductResponseDto::new).collect(
            Collectors.toList()));
    }
}
