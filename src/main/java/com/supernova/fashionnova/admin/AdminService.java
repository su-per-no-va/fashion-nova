package com.supernova.fashionnova.admin;

import com.supernova.fashionnova.answer.Answer;
import com.supernova.fashionnova.answer.AnswerRepository;
import com.supernova.fashionnova.answer.dto.AnswerRequestDto;
import com.supernova.fashionnova.coupon.Coupon;
import com.supernova.fashionnova.coupon.CouponRepository;
import com.supernova.fashionnova.coupon.CouponType;
import com.supernova.fashionnova.coupon.dto.CouponRequestDto;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductDetail;
import com.supernova.fashionnova.product.ProductRepository;
import com.supernova.fashionnova.product.dto.ProductRequestDto;
import com.supernova.fashionnova.question.Question;
import com.supernova.fashionnova.question.QuestionRepository;
import com.supernova.fashionnova.question.dto.QuestionResponseDto;
import com.supernova.fashionnova.review.Review;
import com.supernova.fashionnova.review.ReviewRepository;
import com.supernova.fashionnova.review.dto.ReviewResponseDto;
import com.supernova.fashionnova.upload.FileUploadUtil;
import com.supernova.fashionnova.upload.ImageType;
import com.supernova.fashionnova.user.User;
import com.supernova.fashionnova.user.UserRepository;
import com.supernova.fashionnova.user.dto.UserResponseDto;
import com.supernova.fashionnova.warn.Warn;
import com.supernova.fashionnova.warn.dto.WarnDeleteRequestDto;
import com.supernova.fashionnova.warn.dto.WarnRepository;
import com.supernova.fashionnova.warn.dto.WarnRequestDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private static final int PAGE_SIZE = 30;

    private final UserRepository userRepository;
    private final WarnRepository warnRepository;
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final FileUploadUtil fileUploadUtil;

    /**
     * 유저 전체조회
     *
     * @param page
     * @return List<UserResponseDto>
     * 사이즈는 30으로 고정해놨음
     */
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUserList(int page) {

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
    public void addCaution(WarnRequestDto requestDto) {

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
     * @return List<ReviewResponseDto>
     * @throws CustomException NOT_FOUND_USER 유저ID가 존재하지 않을 때
     */
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewListByUserId(Long userId, int page) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Review> reviewPage = reviewRepository.findByUser(user, pageable);
        List<Review> reviews = reviewPage.getContent();

        // 리뷰 ID 리스트 생성
        List<Long> reviewIds = reviews.stream().map(Review::getId).toList();

        // 리뷰 이미지 다운로드
        Map<Long, List<String>> reviewImages = fileUploadUtil.downloadImages(ImageType.REVIEW, reviewIds);


        // ReviewResponseDto 객체 생성 및 설정
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
        for (Review review : reviews) {
            ReviewResponseDto dto = new ReviewResponseDto(review, reviewImages.get(review.getId())); // byte[] 리스트 설정
            reviewResponseDtos.add(dto);
        }


        return reviewResponseDtos;
    }

    @Transactional
    public void addProduct(ProductRequestDto requestDto) {

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
     * Q&A 답변 등록
     *
     * @param requestDto
     * @throws CustomException NOT_FOUND_QUESTION 문의Id로 문의를 찾을 수 없을 때
     */
    public void addAnswer(AnswerRequestDto requestDto) {

        Question question = questionRepository.findById(requestDto.getQuestionId())
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_QUESTION));

        Answer answer = Answer.builder()
            .question(question)
            .answer(requestDto.getAnswer())
            .build();

        answerRepository.save(answer);

    }

    /**
     * Q&A 문의 전체 조회
     *
     * @param page
     * @return List<QuestionResponseDto>
     */
    public List<QuestionResponseDto> getQuestionList(int page) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Question> questionPage = questionRepository.findAll(pageable);

        return questionPage.stream()
            .map(QuestionResponseDto::new)
            .collect(Collectors.toList());

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
