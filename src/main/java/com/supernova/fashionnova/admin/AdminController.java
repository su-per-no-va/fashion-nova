package com.supernova.fashionnova.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.domain.answer.dto.AnswerRequestDto;
import com.supernova.fashionnova.domain.coupon.dto.CouponRequestDto;
import com.supernova.fashionnova.domain.mileage.dto.MileageRequestDto;
import com.supernova.fashionnova.domain.product.dto.ProductDetailCreateDto;
import com.supernova.fashionnova.domain.product.dto.ProductRequestDto;
import com.supernova.fashionnova.domain.question.dto.QuestionResponseDto;
import com.supernova.fashionnova.domain.review.dto.ReviewResponseDto;
import com.supernova.fashionnova.domain.user.dto.UserResponseDto;
import com.supernova.fashionnova.domain.warn.dto.WarnDeleteRequestDto;
import com.supernova.fashionnova.domain.warn.dto.WarnRequestDto;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import com.supernova.fashionnova.global.util.ResponseUtil;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    /**
     * 판몌통계(일별)
     * */
    @GetMapping("/sold/day")
    public ResponseEntity<String> dailySoldStatistics(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseUtil.of(HttpStatus.OK, adminService.dailySoldStatistics(userDetails.getUser())) ;
    }
    /**
     * 판몌통계(주별)
     * */
    @GetMapping("/sold/week")
    public ResponseEntity<String> weeklySoldStatistics(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseUtil.of(HttpStatus.OK, adminService.weeklySoldStatistics(userDetails.getUser())) ;
    }
    /**
     * 판몌통계(월별)
     * */
    @GetMapping("/sold/moth/{month}")
    public ResponseEntity<String > monthlySoldStatistics(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable int month){
        return ResponseUtil.of(HttpStatus.OK, adminService.monthlySoldStatistics(userDetails.getUser(), month)) ;
    }
    /**
     * 유저 전체 조회
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
    public ResponseEntity<String> addCaution(
        @RequestBody WarnRequestDto requestDto) {

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
    public ResponseEntity<String> deleteCaution(
        @RequestBody WarnDeleteRequestDto requestDto) {

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
     * @param requestDtoJson
     * @return "상품 등록 성공"
     */
    @PostMapping("/products")
    public ResponseEntity<String> addProduct(
        @RequestPart("requestDto") String requestDtoJson,
        @RequestPart List<MultipartFile> files) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ProductRequestDto requestDto = objectMapper.readValue(requestDtoJson, ProductRequestDto.class);

        adminService.addProduct(requestDto,files);

        return ResponseUtil.of(HttpStatus.CREATED, "상품 등록 성공");
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

        adminService.addProductDetails(requestDto.getProductId(),
            requestDto.getProductDetailRequestDtoList());

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
    public ResponseEntity<String> addAnswer(
        @RequestBody AnswerRequestDto requestDto) {

        adminService.addAnswer(requestDto);

        return ResponseUtil.of(HttpStatus.OK, "Q&A 답변 등록 완성");
    }

    /**
     * Q&A 문의 전체 조회
     *
     * @param page
     * @return responseDto
     */
    @GetMapping("/answers")
    public ResponseEntity<List<QuestionResponseDto>> getQuestionList(
        @RequestParam(defaultValue = "0") int page) {

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
    public ResponseEntity<String> addCoupon(
        @Valid @RequestBody CouponRequestDto requestDto) {

        adminService.addCoupon(requestDto);

        return ResponseUtil.of(HttpStatus.CREATED, "쿠폰 지급 성공");
    }

    /**
     * 마일리지 지급
     *
     * @param requestDto
     * @return "마일리지 지급 성공"
     */
    @PostMapping("/mileages")
    public ResponseEntity<String> addMileage(
        @Valid @RequestBody MileageRequestDto requestDto) {

        adminService.addMileage(requestDto);

        return ResponseUtil.of(HttpStatus.CREATED, "마일리지 지급 성공");
    }

    /**
     * 마일리지 초기화
     *
     * @return "마일리지 초기화 성공"
     */
    @DeleteMapping("/mileages")
    public ResponseEntity<String> deleteMileage() {

        adminService.deleteMileage();

        return ResponseUtil.of(HttpStatus.OK, "마일리지 초기화 성공");
    }

    /**
     * 상품 이미지 등록
     *
     * @param file
     * @param productId
     * @return "사진 등록 성공"
     */
    @PostMapping("/products/image/{productId}")
    public ResponseEntity<String> updateProductImage(
        @RequestParam(value = "image") MultipartFile file,
        @PathVariable Long productId) {

        adminService.updateProductImage(file, productId);

       return ResponseUtil.of(HttpStatus.OK,"사진 등록 성공");
    }

}
