package com.supernova.fashionnova.domain.cart;

import com.supernova.fashionnova.domain.cart.dto.CartDeleteRequestDto;
import com.supernova.fashionnova.domain.cart.dto.CartRequestDto;
import com.supernova.fashionnova.domain.cart.dto.CartResponseDto;
import com.supernova.fashionnova.domain.cart.dto.CartUpdateRequestDto;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import com.supernova.fashionnova.global.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
@Slf4j
public class CartController {

    private final CartService cartService;

    /**
     * 장바구니에 상품 추가
     *
     * @param cartRequestDto 장바구니 요청 데이터
     * @param userDetails 인증된 사용자 정보
     * @return "장바구니 담기 완료" 메시지
     */
    @PostMapping
    public ResponseEntity<String> addToCart(
        @Valid @RequestBody CartRequestDto cartRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        cartService.addCart(userDetails.getUser(), cartRequestDto);

        return ResponseUtil.of(HttpStatus.OK, "장바구니 담기 완료");
    }

    /**
     * 장바구니 조회
     *
     * @param userDetails 인증된 사용자 정보
     * @return 장바구니 응답 DTO
     */
    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        CartResponseDto cartResponseDto = cartService.getCart(userDetails.getUser());

        return ResponseUtil.of(HttpStatus.OK, cartResponseDto);
    }

    /**
     * 장바구니 수정
     *
     * @param dto
     * @param userDetails
     * @return "상품 옵션 수정 완료" 메시지
     */
    @PutMapping
    public ResponseEntity<String> updateCart(
        @Valid @RequestBody CartUpdateRequestDto dto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        cartService.updateCart(userDetails.getUser(), dto);

        return ResponseUtil.of(HttpStatus.OK, "상품 옵션 수정 완료");
    }

    /**
     * 장바구니 상품 개별 삭제
     *
     * @param cartDeleteRequestDto
     * @param userDetails
     * @return "장바구니 상품 삭제 완료" 메시지
     */
    @DeleteMapping
    public ResponseEntity<String> deleteFromCart(
        @Valid @RequestBody CartDeleteRequestDto cartDeleteRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        cartService.deleteFromCart(userDetails.getUser(), cartDeleteRequestDto.getProductDetailId());

        return ResponseUtil.of(HttpStatus.OK, "장바구니 상품 삭제 완료");
    }

    /**
     * 장바구니 비우기
     *
     * @param userDetails
     * @return "장바구니 비우기 완료" 메시지
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> clearCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        cartService.clearCart(userDetails.getUser().getId());

        return ResponseUtil.of(HttpStatus.OK, "장바구니 비우기 완료");
    }

}
