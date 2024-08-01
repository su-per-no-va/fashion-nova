package com.supernova.fashionnova.global.faker;

import com.github.javafaker.Faker;
import com.supernova.fashionnova.address.dto.AddressRequestDto;
import com.supernova.fashionnova.answer.dto.AnswerRequestDto;
import com.supernova.fashionnova.cart.dto.CartRequestDto;
import com.supernova.fashionnova.coupon.dto.CouponRequestDto;
import com.supernova.fashionnova.mileage.dto.MileageRequestDto;
import com.supernova.fashionnova.product.ProductCategory;
import com.supernova.fashionnova.product.ProductStatus;
import com.supernova.fashionnova.product.dto.ProductDetailRequestDto;
import com.supernova.fashionnova.product.dto.ProductRequestDto;
import com.supernova.fashionnova.question.dto.QuestionRequestDto;
import com.supernova.fashionnova.review.dto.ReviewRequestDto;
import com.supernova.fashionnova.user.dto.SignupRequestDto;
import com.supernova.fashionnova.wish.dto.WishRequestDto;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

@Component
public class DataGenerator {

    private static final Faker faker = new Faker();

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

    // Warn
    // 직접 경고 - 더미 X

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
                faker.number().randomNumber(5, true), // userId를 위한 랜덤 숫자
                faker.number().numberBetween(0, 10000) // mileage
            ))
            .collect(Collectors.toList());
    }

    // Coupon
    public List<CouponRequestDto> generateCoupons(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> new CouponRequestDto(
                faker.number().randomNumber(5, true), // userId
                faker.commerce().promotionCode(),
                new Date(System.currentTimeMillis() + faker.number().numberBetween(0, 1000000000L)),
                faker.commerce().price(),
                faker.options().option("WELCOME", "GRADE_UP", "REGULAR")
            ))
            .collect(Collectors.toList());
    }

    // Question
    public List<QuestionRequestDto> generateQuestions(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> new QuestionRequestDto(
                faker.lorem().sentence(),
                faker.lorem().paragraph(),
                faker.options().option("PRODUCT", "RESTOCK", "DELIVERY", "SYSTEM", "ORDER_PAYMENT", "EXCHANGE_RETURN")
            ))
            .collect(Collectors.toList());
    }

    // QuestionImage
    // fileName, questionImageUrl, questionId


    // Answer
    public List<AnswerRequestDto> generateAnswers(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> new AnswerRequestDto(
                faker.number().randomNumber(5, true), // questionId
                faker.lorem().paragraph()
            ))
            .collect(Collectors.toList());
    }

    // Product
    public List<ProductRequestDto> generateProducts(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> new ProductRequestDto(
                faker.commerce().productName(),
                faker.number().randomNumber(5, true), // price
                faker.lorem().sentence(),
                faker.options().option(ProductCategory.values()),
                faker.options().option(ProductStatus.values()),
                generateProductDetails(faker.number().numberBetween(1, 5)) // pass productId
            ))
            .collect(Collectors.toList());
    }

    // ProductDetail
    private List<ProductDetailRequestDto> generateProductDetails(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> new ProductDetailRequestDto(
                faker.options().option("S", "M", "L", "XL"), // size
                faker.color().name(), // color
                faker.number().numberBetween(1, 100L), // quantity
                faker.options().option(ProductStatus.values())
            ))
            .collect(Collectors.toList());
    }

    // ProductImage
    // fileName, productImageUrl, productId


    // Wish
    public List<WishRequestDto> generateWishes(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> new WishRequestDto(
                faker.number().randomNumber(5, true) // productId
            ))
            .collect(Collectors.toList());
    }

    // Cart
    public List<CartRequestDto> generateCarts(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> new CartRequestDto(
                faker.number().randomNumber(5, true), // productId
                faker.number().numberBetween(1, 10),
                faker.options().option("S", "M", "L", "XL"),
                faker.color().name()
            ))
            .collect(Collectors.toList());
    }

    // Orders
    // address, cost, discount, invoice, orderStatus, totalPrice, usedMileage, userId


    // OrdersDetails
    // count, price, productName, orderId, productId, productDetailId, userId


    // Review
    public List<ReviewRequestDto> generateReviews(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> new ReviewRequestDto(
                faker.number().randomNumber(5, true), // productId
                faker.lorem().sentence(),
                faker.number().numberBetween(1, 5),
                faker.internet().image()
            ))
            .collect(Collectors.toList());
    }

    // ReviewImage
    // fileName, reviewImageUrl


}
