package com.supernova.fashionnova.wish;

import com.supernova.fashionnova.global.util.ResponseUtil;
import com.supernova.fashionnova.product.dto.ProductResponseDto;
import com.supernova.fashionnova.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlists")
public class WishController {

    private final WishService wishService;

    /** 위시리스트 추가
     *
     * @param userDetails
     * @param productId
     * @return "위시리스트 추가"
     */
    @PostMapping("/{productId}")
    public ResponseEntity<String> addWish(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long productId) {

        wishService.addWish(userDetails.getUser(), productId);

        return ResponseUtil.of(HttpStatus.OK,"위시리스트 추가");
    }

    /** 위시리스트 조회
     *
     * @param userDetails
     * @param page
     * @return Page<ProductResponseDto>
     */
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getWishProductList(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(defaultValue = "0") int page) {

        Page<ProductResponseDto> responseDto = wishService.getWishProductList(userDetails.getUser(), page - 1);

        return ResponseUtil.of(HttpStatus.OK, responseDto);
    }

    /** 위시리스트 삭제
     *
     * @param userDetails
     * @param wishId
     * @return "위시리스트 삭제"
     */
    @DeleteMapping("/{wishId}")
    public ResponseEntity<String> deleteWish(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long wishId) {

        wishService.deleteWish(userDetails.getUser(), wishId);

        return ResponseUtil.of(HttpStatus.OK,"위시리스트 삭제");
    }

}
