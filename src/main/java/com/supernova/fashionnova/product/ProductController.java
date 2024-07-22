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

     @GetMapping("/product")
    public Page<ProductResponseDto> getProductList(@RequestParam(value = "sort") String sorted, @RequestParam(value = "category") String category, @RequestParam(value = "page") int page) {
        return productService.getProductList( page - 1, category, sorted);
    }
}