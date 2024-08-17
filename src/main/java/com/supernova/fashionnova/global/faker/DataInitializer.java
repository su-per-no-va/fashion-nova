package com.supernova.fashionnova.global.faker;

import com.supernova.fashionnova.domain.address.Address;
import com.supernova.fashionnova.domain.address.AddressRepository;
import com.supernova.fashionnova.domain.address.dto.AddressRequestDto;
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
import com.supernova.fashionnova.domain.product.Product;
import com.supernova.fashionnova.domain.product.ProductDetail;
import com.supernova.fashionnova.domain.product.ProductRepository;
import com.supernova.fashionnova.domain.product.dto.ProductRequestDto;
import com.supernova.fashionnova.domain.question.Question;
import com.supernova.fashionnova.domain.question.QuestionRepository;
import com.supernova.fashionnova.domain.question.QuestionType;
import com.supernova.fashionnova.domain.question.dto.QuestionRequestDto;
import com.supernova.fashionnova.domain.review.Review;
import com.supernova.fashionnova.domain.review.ReviewRepository;
import com.supernova.fashionnova.domain.review.dto.ReviewRequestDto;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.domain.user.UserRepository;
import com.supernova.fashionnova.domain.user.dto.SignupRequestDto;
import com.supernova.fashionnova.domain.wish.Wish;
import com.supernova.fashionnova.domain.wish.WishRepository;
import com.supernova.fashionnova.domain.wish.dto.WishRequestDto;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

// @Component
public class DataInitializer implements CommandLineRunner {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final Random random = new Random();

    @Autowired
    private DataGenerator dataGenerator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MileageRepository mileageRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        List<SignupRequestDto> userList = dataGenerator.generateUsers(50);
        List<User> users = saveUsers(userList);

        List<ProductRequestDto> productList = dataGenerator.generateProducts(100);
        List<Product> products = saveProducts(productList);

        List<AddressRequestDto> addressList = dataGenerator.generateAddresses(50);
        saveAddresses(addressList, users);

        List<MileageRequestDto> mileageList = dataGenerator.generateMileages(50);
        saveMileages(mileageList, users);

        List<CouponRequestDto> couponList = dataGenerator.generateCoupons(25);
        saveCoupons(couponList, users);

        List<WishRequestDto> wishList = dataGenerator.generateWishes(300);
        saveWishes(wishList, users, products);

        List<ReviewRequestDto> reviewList = dataGenerator.generateReviews(300);
        saveReviews(reviewList, users, products);

        List<QuestionRequestDto> questionList = dataGenerator.generateQuestions(50);
        List<Question> questions = saveQuestions(questionList, users);

        List<AnswerRequestDto> answerList = dataGenerator.generateAnswers(25);
        saveAnswers(answerList, questions);

    }

    private List<User> saveUsers(List<SignupRequestDto> requestDtoList) {

        List<User> users = requestDtoList.stream()
            .map(dto -> User.builder()
                .userName(dto.getUserName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build())
            .collect(Collectors.toList());

        return userRepository.saveAll(users);

    }

    private List<Product> saveProducts(List<ProductRequestDto> requestDtoList) {

        List<Product> products = requestDtoList.stream()
            .map(dto -> {
                Product product = Product.builder()
                    .product(dto.getProduct())
                    .price(dto.getPrice())
                    .explanation(dto.getExplanation())
                    .category(dto.getCategory())
                    .productStatus(dto.getProductStatus())
                    .build();

                List<ProductDetail> productDetailList = dto.getProductDetailList().stream()
                    .map(productDetailRequestDto -> ProductDetail.builder()
                        .size(productDetailRequestDto.getSize())
                        .color(productDetailRequestDto.getColor())
                        .quantity(productDetailRequestDto.getQuantity())
                        .product(product)
                        .build())
                    .collect(Collectors.toList());

                product.addDetailList(productDetailList);
                return product;
            })
            .collect(Collectors.toList());

        return productRepository.saveAll(products);

    }

    private void saveAddresses(List<AddressRequestDto> requestDtoList, List<User> users) {

        Collections.shuffle(users);

        for (AddressRequestDto dto : requestDtoList) {
            User user = users.get(random.nextInt(users.size()));
            Address address = Address.builder()
                .name(dto.getName())
                .recipientName(dto.getRecipientName())
                .recipientNumber(dto.getRecipientNumber())
                .zipCode(dto.getZipCode())
                .address(dto.getAddress())
                .detail(dto.getDetail())
                .user(user)
                .build();
            addressRepository.save(address);
        }

    }

    private void saveMileages(List<MileageRequestDto> requestDtoList, List<User> users) {

        Collections.shuffle(users);

        for (MileageRequestDto dto : requestDtoList) {
            User user = users.get(random.nextInt(users.size()));
            Mileage mileage = Mileage.builder()
                .user(user)
                .mileage(dto.getMileage())
                .build();
            mileageRepository.save(mileage);
        }

    }

    private void saveCoupons(List<CouponRequestDto> requestDtoList, List<User> users) {

        Collections.shuffle(users);

        for (CouponRequestDto dto : requestDtoList) {
            User user = users.get(random.nextInt(users.size()));
            Coupon coupon = Coupon.builder()
                .user(user)
                .name(dto.getName())
                .period(dto.getPeriod())
                .sale(dto.getSale())
                .type(CouponType.valueOf(dto.getType()))
                .build();
            couponRepository.save(coupon);
        }

    }

    private void saveWishes(List<WishRequestDto> requestDtoList, List<User> users, List<Product> products) {

        Collections.shuffle(users);
        Collections.shuffle(products);

        for (WishRequestDto dto : requestDtoList) {
            User user = users.get(random.nextInt(users.size()));
            Product product = products.get(random.nextInt(products.size()));
            Wish wish = Wish.builder()
                .user(user)
                .product(product)
                .build();
            wishRepository.save(wish);
            product.increaseWish();
        }

    }

    private void saveReviews(List<ReviewRequestDto> requestDtoList, List<User> users, List<Product> products) {

        Collections.shuffle(users);
        Collections.shuffle(products);

        for (ReviewRequestDto dto : requestDtoList) {
            User user = users.get(random.nextInt(users.size()));
            Product product = products.get(random.nextInt(products.size()));
            Review review = Review.builder()
                .user(user)
                .product(product)
                .review(dto.getReview())
                .rating(dto.getRating())
                .build();
            reviewRepository.save(review);
            product.increaseReview();
        }

    }

    private List<Question> saveQuestions(List<QuestionRequestDto> requestDtoList, List<User> users) {

        Collections.shuffle(users);
        List<Question> questions = requestDtoList.stream()
            .map(dto -> {
                User user = users.get(random.nextInt(users.size()));
                return Question.builder()
                    .title(dto.getTitle())
                    .question(dto.getQuestion())
                    .type(QuestionType.valueOf(dto.getType()))
                    .user(user)
                    .build();
            })
            .collect(Collectors.toList());

        return questionRepository.saveAll(questions);

    }

    private void saveAnswers(List<AnswerRequestDto> requestDtoList, List<Question> questions) {

        Collections.shuffle(questions);

        for (int i = 0; i < requestDtoList.size(); i++) {
            if (i >= questions.size()) {
                break;
            }

            Question question = questions.get(i);
            AnswerRequestDto dto = requestDtoList.get(i);

            Answer answer = Answer.builder()
                .question(question)
                .answer(dto.getAnswer())
                .build();

            answerRepository.save(answer);
        }

    }

}