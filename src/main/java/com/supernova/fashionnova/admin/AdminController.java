package com.supernova.fashionnova.admin;

import com.supernova.fashionnova.answer.dto.AnswerRequestDto;
import com.supernova.fashionnova.coupon.dto.CouponRequestDto;
import com.supernova.fashionnova.global.util.ResponseUtil;
import com.supernova.fashionnova.product.dto.ProductDetailCreateDto;
import com.supernova.fashionnova.product.dto.ProductRequestDto;
import com.supernova.fashionnova.question.dto.QuestionResponseDto;
import com.supernova.fashionnova.review.dto.ReviewResponseDto;
import com.supernova.fashionnova.user.dto.UserResponseDto;
import com.supernova.fashionnova.warn.dto.WarnDeleteRequestDto;
import com.supernova.fashionnova.warn.dto.WarnRequestDto;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * 유저 전체조회
     *
     * @param page
     * @return size는 30으로 고정했음
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUserList(
        @RequestParam(defaultValue = "0") int page) {

        List<UserResponseDto> responseDtoList = adminService.getAllUserList(page);

        return ResponseUtil.of(HttpStatus.OK, responseDtoList);
    }

    /**
     * 유저 조회 등록
     *
     * @param requestDto
     * @return "회원 경고 등록 완성"
     */
    @PostMapping("/cautions")
    public ResponseEntity<String> addCaution(@RequestBody WarnRequestDto requestDto) {

        adminService.addCaution(requestDto);

        return ResponseUtil.of(HttpStatus.CREATED, "회원 경고 등록 완성");
    }

    /**
     * 유저 경고 삭제
     *
     * @param requestDto
     * @return "회원 경고 삭제 완료"
     */
    @DeleteMapping("/cautions")
    public ResponseEntity<String> deleteCaution(@RequestBody WarnDeleteRequestDto requestDto) {

        adminService.deleteCaution(requestDto);

        return ResponseUtil.of(HttpStatus.OK, "회원 경고 삭제 완료");
    }

    /**
     * 작성자별 리뷰 조회
     *
     * @param userId
     * @param page
     * @return List<MyReviewResponseDto>
     */
    @GetMapping("/reviews/{userId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewListByUserId(
        @PathVariable Long userId,
        @RequestParam(defaultValue = "0") int page) {

        List<ReviewResponseDto> reviews = adminService.getReviewListByUserId(userId, page);

        return ResponseUtil.of(HttpStatus.OK, reviews);
    }

    /**
     * 상품 등록
     *
     * @param requestDto
     * @return "상품 등록 성공"
     */
    @PostMapping("/products")
    public ResponseEntity<String> addProduct(@RequestBody ProductRequestDto requestDto) {

        adminService.addProduct(requestDto);

        return ResponseUtil.of(HttpStatus.CREATED,"상품 등록 성공");
    }

    /**
     * 상품 디테일 추가
     *
     * @param requestDto
     * @return "상품 디테일 추가 성공"
     */
    @PostMapping("/products/details")
    public ResponseEntity<String> addProductDetails(
        @RequestBody ProductDetailCreateDto requestDto) {

        adminService.addProductDetails(requestDto.getProductId(), requestDto.getProductDetailRequestDtoList());

        return ResponseUtil.of(HttpStatus.OK, "상품 디테일 추가 성공");
    }

    /**
     * 상품 수정
     *
     * @param requestDto
     * @return "상품 수정 성공"
     */
    @PutMapping("/products")
    public ResponseEntity<String> updateProduct(
        @RequestBody ProductRequestDto requestDto) {

        adminService.updateProduct(requestDto);

        return ResponseUtil.of(HttpStatus.OK, "상품 수정 성공");
    }

    /**
     * Q&A 답변 등록
     *
     * @param requestDto
     * @return "Q&A 답변 등록 완성"
     */
    @PostMapping("/answers")
    public ResponseEntity<String> addAnswer(@RequestBody AnswerRequestDto requestDto) {

        adminService.addAnswer(requestDto);

        return ResponseUtil.of(HttpStatus.OK,"Q&A 답변 등록 완성");
    }

    /**
     * Q&A 문의 전체 조회
     *
     * @param page
     * @return responseDto
     */
    @GetMapping("/answers")
    public ResponseEntity<List<QuestionResponseDto>> getQuestionList(@RequestParam(defaultValue = "0") int page) {

        List<QuestionResponseDto> responseDto = adminService.getQuestionList(page);

        return ResponseUtil.of(HttpStatus.OK, responseDto);
    }

    /**
     * 쿠폰 지급
     *
     * @param requestDto
     * @return "쿠폰 지급 성공"
     */
    @PostMapping("/coupons")
    public ResponseEntity<String> addCoupon(@Valid @RequestBody CouponRequestDto requestDto) {

        adminService.addCoupon(requestDto);

        return ResponseUtil.of(HttpStatus.CREATED,"쿠폰 지급 성공");
    }

}
