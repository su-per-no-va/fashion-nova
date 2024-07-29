package com.supernova.fashionnova.product;


import com.supernova.fashionnova.product.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")

public class ProductController {

     private final ProductService productService;

    /**
     * 조건별 상품 검색
     * @param sorted
     * @param category
     * @param page
     * @return 상품 리스트
     */

     @GetMapping("/product")
    public Page<ProductResponseDto> getProductList(@RequestParam(value = "sort") String sorted,
         @RequestParam(value = "category") String category,
         @RequestParam(value = "size") String size,
         @RequestParam(value = "color") String color,
         @RequestParam(value = "page") int page) {

        return productService.getProductList( page - 1, category, size, color, sorted);
    }

}