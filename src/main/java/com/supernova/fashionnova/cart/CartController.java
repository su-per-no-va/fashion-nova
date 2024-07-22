package com.supernova.fashionnova.cart;

import com.supernova.fashionnova.cart.dto.CartRequestDto;
import com.supernova.fashionnova.cart.dto.CartResponseDto;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.user.User;
import com.supernova.fashionnova.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    private final UserRepository userRepository;

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
        @AuthenticationPrincipal UserDetails userDetails) {

        // userDetails에서 username을 통해 User 객체를 가져옵니다.
        User user = userRepository.findByUserName(userDetails.getUsername())
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));

        cartService.addCart(user, cartRequestDto.getProductDetailsId(), cartRequestDto.getCount());

        return new ResponseEntity<>("장바구니 담기 완료", HttpStatus.OK);
    }

    /**
     * 장바구니 조회
     *
     * @param userDetails 인증된 사용자 정보
     * @return 장바구니 응답 DTO
     */
    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        // userDetails에서 username을 통해 User 객체를 가져옵니다.
        User user = userRepository.findByUserName(userDetails.getUsername())
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));

        CartResponseDto cartResponseDto = cartService.getCart(user);
        return new ResponseEntity<>(cartResponseDto, HttpStatus.OK);
    }
}
