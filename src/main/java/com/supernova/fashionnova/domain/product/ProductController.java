package com.supernova.fashionnova.domain.product;


import com.supernova.fashionnova.domain.product.dto.ProductResponseDto;
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
         @RequestParam(value = "category", required = false) String category,
         @RequestParam(value = "size", required = false) String size,
         @RequestParam(value = "color", required = false) String color,
         @RequestParam(value = "search", required = false) String search,
         @RequestParam(value = "page", required = false) int page) {
         System.out.println("[[[[[[[[[[[[" + search);

        return productService.getProductList(page - 1, category, size, color, search, sorted);
    }

}