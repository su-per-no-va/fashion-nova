package com.supernova.fashionnova.wish;

import com.supernova.fashionnova.global.util.ResponseUtil;
import com.supernova.fashionnova.product.dto.ProductResponseDto;
import com.supernova.fashionnova.security.UserDetailsImpl;
import com.supernova.fashionnova.wish.dto.WishDeleteRequestDto;
import com.supernova.fashionnova.wish.dto.WishRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * @param requestDto
     * @return "위시리스트 추가"
     */
    @PostMapping
    public ResponseEntity<String> addWish(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody WishRequestDto requestDto) {

        wishService.addWish(userDetails.getUser(), requestDto);

        return ResponseUtil.of(HttpStatus.OK,"위시리스트 추가");
    }

    /** 위시리스트 조회
     *
     * @param userDetails
     * @param page
     * @return Page<ProductResponseDto>
     */
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getWishProductPage(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam int page) {

        Page<ProductResponseDto> responseDto = wishService.getWishProductPage(userDetails.getUser(), page - 1);

        return ResponseUtil.of(HttpStatus.OK, responseDto);
    }

    /** 위시리스트 삭제
     *
     * @param userDetails
     * @param requestDto
     * @return "위시리스트 삭제"
     */
    @DeleteMapping
    public ResponseEntity<String> deleteWish(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody WishDeleteRequestDto requestDto) {

        wishService.deleteWish(userDetails.getUser(), requestDto);

        return ResponseUtil.of(HttpStatus.OK,"위시리스트 삭제");
    }

}
