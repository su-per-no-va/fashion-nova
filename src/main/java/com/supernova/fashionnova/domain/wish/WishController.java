package com.supernova.fashionnova.domain.wish;

import com.supernova.fashionnova.domain.wish.dto.WishDeleteRequestDto;
import com.supernova.fashionnova.domain.wish.dto.WishRequestDto;
import com.supernova.fashionnova.domain.wish.dto.WishResponseDto;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import com.supernova.fashionnova.global.util.ResponseUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

    /**
     * 위시리스트 추가
     *
     * @param userDetails
     * @param requestDto
     * @return "위시리스트 추가"
     */
    @PostMapping
    public ResponseEntity<String> addWish(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody WishRequestDto requestDto) {

        wishService.addWish(userDetails.getUser(), requestDto);

        return ResponseUtil.of(HttpStatus.CREATED,"위시리스트 추가");
    }

    /**
     * 위시리스트 조회
     *
     * @param userDetails
     * @param page
     * @return List<ProductResponseDto>
     */
    @GetMapping
    public ResponseEntity<List<WishResponseDto>> getWishProductList(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(defaultValue = "0") int page) {

        List<WishResponseDto> responseDto = wishService.getWishProductList(userDetails.getUser(), page);

        return ResponseUtil.of(HttpStatus.OK, responseDto);
    }

    /**
     * 위시리스트 삭제
     *
     * @param userDetails
     * @param requestDto
     * @return "위시리스트 삭제"
     */
    @DeleteMapping
    public ResponseEntity<String> deleteWish(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody WishDeleteRequestDto requestDto) {

        wishService.deleteWish(userDetails.getUser(), requestDto);

        return ResponseUtil.of(HttpStatus.OK,"위시리스트 삭제");
    }

}
