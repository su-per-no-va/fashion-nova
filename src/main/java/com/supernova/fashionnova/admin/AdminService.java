package com.supernova.fashionnova.admin;

import com.supernova.fashionnova.address.dto.AddressRequestDto;
import com.supernova.fashionnova.coupon.Coupon;
import com.supernova.fashionnova.coupon.CouponRepository;
import com.supernova.fashionnova.coupon.CouponType;
import com.supernova.fashionnova.coupon.dto.CouponRequestDto;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.review.Review;
import com.supernova.fashionnova.review.ReviewRepository;
import com.supernova.fashionnova.review.dto.ReviewResponseDto;
import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductDetail;
import com.supernova.fashionnova.product.ProductDetailRepository;
import com.supernova.fashionnova.product.ProductRepository;
import com.supernova.fashionnova.product.dto.ProductRequestDto;
import com.supernova.fashionnova.user.User;
import com.supernova.fashionnova.user.UserRepository;
import com.supernova.fashionnova.user.dto.UserResponseDto;
import com.supernova.fashionnova.warn.Warn;
import com.supernova.fashionnova.warn.dto.WarnDeleteRequestDto;
import com.supernova.fashionnova.warn.dto.WarnRepository;
import com.supernova.fashionnova.warn.dto.WarnRequestDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    private final WarnRepository warnRepository;

    private final ReviewRepository reviewRepository;

    private static final int PAGE_SIZE = 30;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final CouponRepository couponRepository;

    /**
     * 유저 전체조회
     *
     * @param page
     * @return List<UserResponseDto>
     * 사이즈는 30으로 고정해놨음
     */
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers(int page) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<User> userPage = userRepository.findAll(pageable);

        return userPage.stream()
            .map(UserResponseDto::new)
            .collect(Collectors.toList());
    }

    /**
     * 유저 경고 등록
     *
     * @param requestDto
     * @throws CustomException NOT_FOUND_USER 유저Id로 유저를 찾을 수 없을 때
     */
    public void createCaution(WarnRequestDto requestDto) {

        User user = userRepository.findById(requestDto.getUserId())
            .orElseThrow(()-> new CustomException(ErrorType.NOT_FOUND_USER));

        Warn warn = new Warn(requestDto.getDetail(),user);
        warnRepository.save(warn);
    }

    /**
     * 유저 경고 삭제
     *
     * @param requestDto
     * @throws CustomException NOT_FOUND_WARN 경고ID로 경고를 찾을 수 없을 때
     */
    @Transactional
    public void deleteCaution(WarnDeleteRequestDto requestDto) {

        Warn warn = warnRepository.findById(requestDto.getWarnId())
            .orElseThrow(()-> new CustomException(ErrorType.NOT_FOUND_WARN));

        warnRepository.delete(warn);
    }

    /**
     * 작성자별 리뷰 조회
     *
     * @param userId
     * @param page
     * @return Page<Review>
     * @throws CustomException NOT_FOUND_USER 유저ID가 존재하지 않을 때
     */
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByUserId(Long userId, int page) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Review> reviewPage = reviewRepository.findByUser(user, pageable);

        return reviewPage.stream()
            .map(ReviewResponseDto::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public void createProduct(ProductRequestDto requestDto) {

        Product product = Product.builder()
            .product(requestDto.getProduct())
            .price(requestDto.getPrice())
            .explanation(requestDto.getExplanation())
            .category(requestDto.getCategory())
            .productStatus(requestDto.getProductStatus())
            .build();
        List<ProductDetail> productDetailList = (requestDto.getProductDetailList().stream()
            .map(productDetailRequestDto -> {
                return ProductDetail.builder()
                    .size(productDetailRequestDto.getSize())
                    .color(productDetailRequestDto.getColor())
                    .quantity(productDetailRequestDto.getQuantity())
                    .product(product)
                    .build();
            })
            .collect(Collectors.toList()));
        product.addDetail(productDetailList);
        productRepository.save(product);
        /*productDetailRepository.saveAll(product.getProductDetailList());*/
    }

    /**
     * 쿠폰 지급
     *
     * @param requestDto
     * @throws CustomException NOT_FOUND_USER 유저ID가 존재하지 않을 때
     */
    public void addCoupon(CouponRequestDto requestDto) {

        User user = userRepository.findById(requestDto.getUserId())
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));

        Coupon coupon = Coupon.builder()
            .user(user)
            .name(requestDto.getName())
            .period(requestDto.getPeriod())
            .sale(requestDto.getSale())
            .type(CouponType.valueOf(requestDto.getType()))
            .build();

        couponRepository.save(coupon);

    }

}
