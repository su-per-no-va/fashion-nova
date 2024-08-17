package com.supernova.fashionnova.admin;

import com.supernova.fashionnova.admin.dto.AllReviewResponseDto;
import com.supernova.fashionnova.admin.dto.UserProfileResponseDto;
import com.supernova.fashionnova.admin.dto.UsersCouponAndMileageResponseDto;
import com.supernova.fashionnova.domain.address.Address;
import com.supernova.fashionnova.domain.address.AddressRepository;
import com.supernova.fashionnova.domain.address.dto.AddressResponseDto;
import com.supernova.fashionnova.domain.answer.Answer;
import com.supernova.fashionnova.domain.answer.AnswerRepository;
import com.supernova.fashionnova.domain.answer.dto.AnswerRequestDto;
import com.supernova.fashionnova.domain.coupon.Coupon;
import com.supernova.fashionnova.domain.coupon.CouponRepository;
import com.supernova.fashionnova.domain.coupon.CouponType;
import com.supernova.fashionnova.domain.coupon.dto.CouponRequestDto;
import com.supernova.fashionnova.domain.mileage.Mileage;
import com.supernova.fashionnova.domain.mileage.MileageRepository;
import com.supernova.fashionnova.domain.mileage.dto.MileageRequestDto;
import com.supernova.fashionnova.domain.order.Order;
import com.supernova.fashionnova.domain.order.OrdersRepository;
import com.supernova.fashionnova.domain.product.Product;
import com.supernova.fashionnova.domain.product.ProductDetail;
import com.supernova.fashionnova.domain.product.ProductRepository;
import com.supernova.fashionnova.domain.product.ProductStatus;
import com.supernova.fashionnova.domain.product.dto.ProductDetailRequestDto;
import com.supernova.fashionnova.domain.product.dto.ProductRequestDto;
import com.supernova.fashionnova.domain.question.Question;
import com.supernova.fashionnova.domain.question.QuestionRepository;
import com.supernova.fashionnova.domain.question.QuestionStatus;
import com.supernova.fashionnova.domain.question.dto.QuestionResponseDto;
import com.supernova.fashionnova.domain.review.Review;
import com.supernova.fashionnova.domain.review.ReviewRepository;
import com.supernova.fashionnova.domain.review.dto.ReviewResponseDto;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.domain.user.UserRepository;
import com.supernova.fashionnova.domain.user.UserRole;
import com.supernova.fashionnova.domain.user.UserStatus;
import com.supernova.fashionnova.domain.user.dto.UserResponseDto;
import com.supernova.fashionnova.domain.warn.Warn;
import com.supernova.fashionnova.domain.warn.WarnRepository;
import com.supernova.fashionnova.domain.warn.dto.WarnDeleteRequestDto;
import com.supernova.fashionnova.domain.warn.dto.WarnRequestDto;
import com.supernova.fashionnova.domain.warn.dto.WarnResponseDto;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.global.upload.FileUploadUtil;
import com.supernova.fashionnova.global.upload.ImageType;
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
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AdminService {

    private static final int PAGE_SIZE = 30;

    private final AddressRepository addressRepository;
    private final AnswerRepository answerRepository;
    private final CouponRepository couponRepository;
    private final MileageRepository mileageRepository;
    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;
    private final QuestionRepository questionRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final WarnRepository warnRepository;

    private final FileUploadUtil fileUploadUtil;

    /**
     * 판매 통계 (일별)
     *
     * @param user
     * @return total + " 원 입니다."
     * @throws CustomException DENIED_PERMISSION
     */
    public String dailySoldStatistics(User user) {

        if (!UserRole.ADMIN.equals(user.getUserRole())) {
            throw new CustomException(ErrorType.DENIED_PERMISSION);
        }

        return ordersRepository.findTodayOrderTotalPriceSum().map(total -> total + " 원 입니다.")
            .orElse("0원 입니다.");
    }

    /**
     * 판매 통계 (주별)
     *
     * @param user
     * @return total + " 원 입니다."
     * @throws CustomException DENIED_PERMISSION
     */
    public String weeklySoldStatistics(User user) {

        if (!UserRole.ADMIN.equals(user.getUserRole())) {
            throw new CustomException(ErrorType.DENIED_PERMISSION);
        }

        return ordersRepository.findWeekOrderTotalPriceSum().map(total -> total + " 원 입니다.")
            .orElse("0원 입니다.");
    }

    /**
     * 판매 통계 (월별)
     *
     * @param user
     * @param month
     * @return total + " 원 입니다."
     */
    public String monthlySoldStatistics(User user, int month) {

        if (!UserRole.ADMIN.equals(user.getUserRole())) {
            throw new CustomException(ErrorType.DENIED_PERMISSION);
        }

        if (month < 0 || month > 12) {
            throw new CustomException(ErrorType.WRONG_MONTH);
        }

        return ordersRepository.findMonthOrderTotalPriceSum(month).map(total -> total + " 원 입니다.")
            .orElse("0원 입니다.");
    }

    /**
     * 유저 전체 조회
     *
     * @param page
     * @return List<UserResponseDto>
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
     * 유저 프로필 조회
     *
     * @param userId
     * @return UserProfileResponseDto
     * @throws CustomException NOT_FOUND_USER
     */
    public UserProfileResponseDto getUserProfile(Long userId) {

        User user = getUser(userId);

        // 경고리스트 -> WarnResponseDto 형태로변환
        List<Warn> warnList = warnRepository.findByUser(user);
        List<WarnResponseDto> warnResponseDtoList =
            warnList.stream().map(WarnResponseDto::new).toList();

        // 주소리스트 -> AddressResponseDto 형태로변환
        List<Address> addressList = addressRepository.findByUser(user);
        List<AddressResponseDto> addressResponseDtoList =
            addressList.stream().map(AddressResponseDto::new).toList();

        // 오더 찾아오기
        List<Order> orderList = ordersRepository.findAllByUserId(userId);

        // 누적금액과 최근 주문 ID 초기화
        Long totalPrice = 0L;
        Long recentOrderId = 0L;

        // 최근 주문을 찾기 위한 변수
        Order recentOrder = null;

        for (Order order : orderList) {
            // 누적금액 더하기
            totalPrice += order.getTotalPrice();

            // 최근 주문 찾기
            if (recentOrder == null || order.getCreatedAt().isAfter(recentOrder.getCreatedAt())) {
                recentOrder = order;
            }
        }

        // 최근 주문의 ID 가져오기 (최근 주문이 있을 경우)
        if (recentOrder != null) {
            recentOrderId = recentOrder.getId();
        }

        return new UserProfileResponseDto(user, warnResponseDtoList, addressResponseDtoList, totalPrice, recentOrderId);
    }

    /**
     * 유저 쿠폰 & 마일리지 조회
     *
     * @param page
     * @return List<UsersCouponAndMileageResponseDto>
     */
    public List<UsersCouponAndMileageResponseDto> getAllUsersCouponAndMileages(int page) {

        // 유저 찾기
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<User> userPage = userRepository.findAll(pageable);

        return userPage.stream()
            .map(UsersCouponAndMileageResponseDto::new)
            .collect(Collectors.toList());
    }

    /**
     * 유저 경고 등록
     *
     * @param requestDto
     * @throws CustomException NOT_FOUND_USER 유저Id로 유저를 찾을 수 없을 때
     */
    @Transactional
    public void addCaution(WarnRequestDto requestDto) {

        User user = getUser(requestDto.getUserId());
        Warn warn = new Warn(requestDto.getDetail(), user);

        warnRepository.save(warn);

        int warnCount = warnRepository.countByUser(user);

        if (warnCount == 3) {
            user.updateStatus(UserStatus.BLOCKED_MEMBER);
        }

    }

    /**
     * 유저 경고 삭제
     *
     * @param requestDto
     * @throws CustomException NOT_FOUND_WARN 경고ID로 경고를 찾을 수 없을 때
     */
    @Transactional
    public void deleteCaution(WarnDeleteRequestDto requestDto) {

        Warn warn = getWarn(requestDto.getWarnId());

        warnRepository.delete(warn);
    }

    /**
     *  전체 리뷰 조회
     *
     * @param page
     * @return List<AllReviewResponseDto>
     */
    public List<AllReviewResponseDto> getAllRevivewList(int page) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Review> reviewPage = reviewRepository.findAll(pageable);

        return reviewPage.stream().map(AllReviewResponseDto::new).toList();
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

        User user = getUser(userId);

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Review> reviewPage = reviewRepository.findByUser(user, pageable);
        List<Review> reviews = reviewPage.getContent();

        // 리뷰 ID 리스트 생성
        List<Long> reviewIds = reviews.stream().map(Review::getId).toList();

        // 리뷰 이미지 다운로드
        Map<Long, List<String>> reviewImages = fileUploadUtil.downloadImages(ImageType.REVIEW,
            reviewIds);

        // ReviewResponseDto 객체 생성 및 설정
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
        for (Review review : reviews) {
            ReviewResponseDto dto = new ReviewResponseDto(review, reviewImages.get(review.getId()));
            reviewResponseDtos.add(dto);
        }

        return reviewResponseDtos;
    }

    /**
     * 상품 등록
     *
     * @param requestDto
     * @param files
     */
    @Transactional
    public void addProduct(ProductRequestDto requestDto, List<MultipartFile> files) {

        Product product = Product.builder()
            .product(requestDto.getProduct())
            .price(requestDto.getPrice())
            .explanation(requestDto.getExplanation())
            .category(requestDto.getCategory())
            .productStatus(ProductStatus.ACTIVE)
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

        product.addDetailList(productDetailList);
        productRepository.save(product);

        // 파일 업로드
        fileUploadUtil.uploadImage(files, ImageType.PRODUCT, product.getId());
    }

    /**
     * 상품 디테일 추가
     *
     * @param productId
     * @param productDetailRequestDto
     * @throws CustomException NOT_FOUND_PRODUCT 상품 정보가 존재하지 않을 때
     */
    @Transactional
    public void addProductDetails(Long productId, List<ProductDetailRequestDto> productDetailRequestDto) {

        Product product = getProduct(productId);

        for (ProductDetailRequestDto detailDto : productDetailRequestDto) {
            ProductDetail productDetail = ProductDetail.builder()
                .size(detailDto.getSize())
                .color(detailDto.getColor())
                .quantity(detailDto.getQuantity())
                .product(product)
                .build();

            product.addDetail(productDetail);
        }

        productRepository.save(product);
    }

    /**
     * 상품 수정
     *
     * @param requestDto
     * @throws CustomException NOT_FOUND_PRODUCT 상품 정보가 존재하지 않을 때
     */
    @Transactional
    public void updateProduct(ProductRequestDto requestDto) {

        Product existingProduct = getProduct(requestDto.getProductId());
        List<ProductDetailRequestDto> newProductDetails = requestDto.getProductDetailList();

        List<ProductDetail> productDetailList = existingProduct.getProductDetailList();

        for (ProductDetailRequestDto productDetailRequestDto : newProductDetails) {

            ProductDetail productDetail = productDetailList.stream()
                .filter(p -> p.getId().equals(productDetailRequestDto.getProductDetailId()))
                .findFirst().orElse(null);

            if (productDetail != null) {

                productDetail.updateDetail(
                    productDetailRequestDto.getSize(),
                    productDetailRequestDto.getColor(),
                    productDetailRequestDto.getQuantity(),
                    productDetailRequestDto.getStatus()
                );

            }

        }

        existingProduct.updateProduct(
            requestDto.getProduct(),
            requestDto.getPrice(),
            requestDto.getExplanation(),
            requestDto.getCategory(),
            requestDto.getProductStatus()
        );

        productRepository.save(existingProduct);
    }

    /**
     * 상품 이미지 등록
     *
     * @param file
     * @param productId
     * @throws CustomException NOT_FOUND_PRODUCT
     */
    public void updateProductImage(MultipartFile file, Long productId) {

        Product product = getProduct(productId);
        List<MultipartFile> files = List.of(file);

        fileUploadUtil.uploadImage(files, ImageType.PRODUCT, productId);
    }

    /**
     * 답변 등록
     *
     * @param requestDto
     * @throws CustomException NOT_FOUND_QUESTION 문의Id로 문의를 찾을 수 없을 때
     */
    @Transactional
    public void addAnswer(AnswerRequestDto requestDto) {

        Question question = getQuestion(requestDto.getQuestionId());

        if (question.getStatus().equals(QuestionStatus.COMPLETED)) {
            throw new CustomException(ErrorType.ANSWER_ALREADY_EXISTS);
        }

        Answer answer = Answer.builder()
            .question(question)
            .answer(requestDto.getAnswer())
            .build();

        answerRepository.save(answer);

        question.updateQuestionStatus();
    }

    /**
     * 문의 전체 조회
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

        User user = getUser(requestDto.getUserId());

        Coupon coupon = Coupon.builder()
            .user(user)
            .name(requestDto.getName())
            .period(requestDto.getPeriod())
            .sale(requestDto.getSale())
            .type(CouponType.valueOf(requestDto.getType()))
            .build();

        couponRepository.save(coupon);
    }

    /**
     * 마일리지 지급
     *
     * @param requestDto
     * @throws CustomException NOT_FOUND_USER 유저ID가 존재하지 않을 때
     */
    @Transactional
    public void addMileage(MileageRequestDto requestDto) {

        User user = getUser(requestDto.getUserId());

        Mileage mileage = Mileage.builder()
            .user(user)
            .mileage(requestDto.getMileage())
            .build();

        mileageRepository.save(mileage);
    }

    /**
     * 마일리지 초기화
     */
    @Transactional
    public void deleteMileage() {

        mileageRepository.deleteAll();
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));
    }

    private Warn getWarn(Long warnId) {
        return warnRepository.findById(warnId)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_WARN));
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));
    }

    private Question getQuestion(Long questionId) {
        return questionRepository.findById(questionId)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_QUESTION));
    }

}
