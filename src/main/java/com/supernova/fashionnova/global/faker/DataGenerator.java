package com.supernova.fashionnova.global.faker;

import com.github.javafaker.Faker;
import com.supernova.fashionnova.domain.address.dto.AddressRequestDto;
import com.supernova.fashionnova.domain.answer.dto.AnswerRequestDto;
import com.supernova.fashionnova.domain.coupon.dto.CouponRequestDto;
import com.supernova.fashionnova.domain.mileage.dto.MileageRequestDto;
import com.supernova.fashionnova.domain.product.ProductCategory;
import com.supernova.fashionnova.domain.product.ProductStatus;
import com.supernova.fashionnova.domain.product.dto.ProductDetailRequestDto;
import com.supernova.fashionnova.domain.product.dto.ProductRequestDto;
import com.supernova.fashionnova.domain.question.dto.QuestionRequestDto;
import com.supernova.fashionnova.domain.review.dto.ReviewRequestDto;
import com.supernova.fashionnova.domain.user.dto.SignupRequestDto;
import com.supernova.fashionnova.domain.wish.dto.WishRequestDto;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

@Component
public class DataGenerator {

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    // User
    public List<SignupRequestDto> generateUsers(int count) {

        return IntStream.range(0, count)
            .mapToObj(i -> new SignupRequestDto(
                faker.regexify("[a-zA-Z0-9]{4,15}"),
                faker.regexify("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,100}$"),
                faker.name().fullName(),
                faker.internet().emailAddress(),
                faker.regexify("010-\\d{4}-\\d{4}")
            ))
            .collect(Collectors.toList());

    }

    // Product
    public List<ProductRequestDto> generateProducts(int count) {

        return IntStream.range(0, count)
            .mapToObj(i -> new ProductRequestDto(
                faker.commerce().productName(),
                faker.number().randomNumber(5, true),
                faker.lorem().sentence(),
                faker.options().option(ProductCategory.values()),
                faker.options().option(ProductStatus.values()),
                generateProductDetails(faker.number().numberBetween(1, 5))
            ))
            .collect(Collectors.toList());

    }

    // ProductDetail
    private List<ProductDetailRequestDto> generateProductDetails(int count) {

        return IntStream.range(0, count)
            .mapToObj(i -> new ProductDetailRequestDto(
                faker.options().option("S", "M", "L", "XL"),
                faker.color().name(),
                faker.number().numberBetween(1, 100L),
                faker.options().option(ProductStatus.values())
            ))
            .collect(Collectors.toList());

    }

    // Address
    public List<AddressRequestDto> generateAddresses(int count) {

        return IntStream.range(0, count)
            .mapToObj(i -> new AddressRequestDto(
                faker.name().fullName(),
                faker.address().streetName(),
                faker.regexify("010-\\d{4}-\\d{4}"),
                faker.regexify("\\d{5}"),
                faker.address().fullAddress(),
                faker.address().secondaryAddress()
            ))
            .collect(Collectors.toList());

    }

    // Mileage
    public List<MileageRequestDto> generateMileages(int count) {

        return IntStream.range(0, count)
            .mapToObj(i -> new MileageRequestDto(
                faker.number().randomNumber(5, true),
                (long) faker.number().numberBetween(1, 50) * 10
            ))
            .collect(Collectors.toList());

    }

    // Coupon
    public List<CouponRequestDto> generateCoupons(int count) {

        return IntStream.range(0, count)
            .mapToObj(i -> new CouponRequestDto(
                faker.number().randomNumber(5, true),
                faker.commerce().promotionCode(),
                new Date(System.currentTimeMillis() + faker.number().numberBetween(0, 1000000000L)),
                random.nextInt(10) + "%",
                faker.options().option("WELCOME", "GRADE_UP", "REGULAR")
            ))
            .collect(Collectors.toList());

    }

    // Wish
    public List<WishRequestDto> generateWishes(int count) {

        return IntStream.range(0, count)
            .mapToObj(i -> new WishRequestDto(
                faker.number().randomNumber(5, true)
            ))
            .collect(Collectors.toList());

    }

    // Review
    public List<ReviewRequestDto> generateReviews(int count) {

        return IntStream.range(0, count)
            .mapToObj(i -> new ReviewRequestDto(
                faker.number().randomNumber(5, true),
                faker.lorem().sentence(),
                faker.number().numberBetween(1, 5)
            ))
            .collect(Collectors.toList());

    }

    // Question
    public List<QuestionRequestDto> generateQuestions(int count) {

        return IntStream.range(0, count)
            .mapToObj(i -> QuestionRequestDto.builder()
                .title(faker.lorem().sentence())
                .question(faker.lorem().paragraph())
                .type(faker.options().option("PRODUCT", "RESTOCK", "DELIVERY", "SYSTEM", "ORDER_PAYMENT", "EXCHANGE_RETURN"))
                .build()
            )
            .collect(Collectors.toList());

    }

    // Answer
    public List<AnswerRequestDto> generateAnswers(int count) {

        return IntStream.range(0, count)
            .mapToObj(i -> new AnswerRequestDto(
                faker.number().randomNumber(5, true),
                faker.lorem().paragraph()
            ))
            .collect(Collectors.toList());

    }

    // Cart

    // Orders
    // address, cost, discount, invoice, orderStatus, totalPrice, usedMileage, userId

    // OrdersDetails
    // count, price, productName, orderId, productId, productDetailId, userId

    // ProductImage
    // fileName, productImageUrl, productId

    // QuestionImage
    // fileName, questionImageUrl, questionId

    // ReviewImage
    // fileName, reviewImageUrl

    // Warn
    // 직접 경고 - 더미 X

}
