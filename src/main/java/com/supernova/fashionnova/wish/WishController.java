package com.supernova.fashionnova.wish;

import com.supernova.fashionnova.global.util.ResponseUtil;
import com.supernova.fashionnova.security.UserDetailsImpl;
import com.supernova.fashionnova.wish.dto.WishResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishlists")
public class WishController {

    private final WishService wishService;

    /** 위시리스트 찜
     *
     * @param userDetails
     * @param productId
     * @return "위시리스트 추가" OR "위시리스트 삭제"
     */
    @PutMapping("/{productId}")
    public ResponseEntity<String> updateWish(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long productId) {

        boolean isWish = wishService.updateWish(userDetails.getUser(), productId);

        if (isWish) {
            return ResponseUtil.of(HttpStatus.OK,"위시리스트 추가");
        } else {
            return ResponseUtil.of(HttpStatus.OK,"위시리스트 삭제");
        }
    }

    /** 위시리스트 조회
     *
     * @param userDetails
     * @param page
     * @return Page<WishResponseDto>
     */
    @GetMapping
    public ResponseEntity<Page<WishResponseDto>> getWishProductList(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(defaultValue = "0") int page) {

        Page<WishResponseDto> responseDto = wishService.getWishProductList(userDetails.getUser(), page - 1);

        return ResponseUtil.of(HttpStatus.OK, responseDto);
    }

}
