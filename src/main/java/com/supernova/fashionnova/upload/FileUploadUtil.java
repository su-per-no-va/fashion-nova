package com.supernova.fashionnova.upload;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductImage;
import com.supernova.fashionnova.product.ProductImageRepository;
import com.supernova.fashionnova.product.ProductRepository;
import com.supernova.fashionnova.question.Question;
import com.supernova.fashionnova.question.QuestionImage;
import com.supernova.fashionnova.question.QuestionImageRepository;
import com.supernova.fashionnova.question.QuestionRepository;
import com.supernova.fashionnova.review.Review;
import com.supernova.fashionnova.review.ReviewImage;
import com.supernova.fashionnova.review.ReviewImageRepository;
import com.supernova.fashionnova.review.ReviewRepository;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class FileUploadUtil {

    private final AmazonS3Client amazonS3Client;

    private final ReviewImageRepository reviewImageRepository;

    private final QuestionImageRepository questionImageRepository;

    private final ReviewRepository reviewRepository;

    private final ProductRepository productRepository;

    private final ProductImageRepository productImageRepository;

    private final QuestionRepository questionRepository;

    private String random = UUID.randomUUID().toString().substring(0, 10);

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    @Transactional
    public void uploadImage(List<MultipartFile> files, ImageType type, Long typeId) {

        ArrayList<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename(); // 원본 파일 명
            String s3FileName = random + originalFilename; // 변경된 파일 명 (같은 이름의 파일 방지)

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            try {
                amazonS3Client.putObject(bucket, s3FileName, file.getInputStream(), metadata); // 파일 업로드

                imageUrls.add(amazonS3Client.getUrl(bucket, s3FileName).toString());

            } catch (IOException e) {
                throw new CustomException(ErrorType.UPLOAD_REVIEW);
            }
        }


        switch (type) {
            case REVIEW -> {
                Review review = reviewRepository.findById(typeId).orElseThrow(() ->
                    new CustomException(ErrorType.NOT_FOUND_REVIEW));

                for (String imageUrl : imageUrls) {
                    ReviewImage reviewImage = new ReviewImage(review, imageUrl);
                    reviewImageRepository.save(reviewImage);
                }

            }

            case PRODUCT -> {
                Product product = productRepository.findById(typeId).orElseThrow(() ->
                    new CustomException(ErrorType.NOT_FOUND_PRODUCT));

                for (String imageUrl : imageUrls) {
                    ProductImage productImage = new ProductImage(product, imageUrl);
                    productImageRepository.save(productImage);
                }
            }

            case QUESTION -> {
                Question question = questionRepository.findById(typeId).orElseThrow(() ->
                    new CustomException(ErrorType.NOT_FOUND_QUESTION));
                for (String imageUrl : imageUrls) {
                    QuestionImage questionImage = new QuestionImage(question, imageUrl);
                    questionImageRepository.save(questionImage);
                    question.getQuestionImageUrls().add(questionImage);
                }

            }
        }
    }

    public Map<Long, List<String>> downloadImages(ImageType type, List<Long> typeIds) {
        Map<Long, List<String>> imagesMap = new HashMap<>();

        for (Long id : typeIds) {
            List<String> imageUrls = new ArrayList<>();
            switch (type) {
                case REVIEW -> {
                    List<ReviewImage> reviewImages = reviewImageRepository.findAllByReviewId(id);
                    for (ReviewImage reviewImage : reviewImages) {
                    imageUrls.add(reviewImage.getReviewImageUrl());
                    }
                }
                case PRODUCT -> {
                    Product product = productRepository.findById(id).orElseThrow(
                        () -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));

                    List<ProductImage> productImages = productImageRepository.findAllByProduct(
                        product);
                    for (ProductImage productImage : productImages) {
                        imageUrls.add(productImage.getProductImageUrl());
                    }
                }
                case QUESTION -> {
                    Question question = questionRepository.findById(id).orElseThrow(
                        () -> new CustomException(ErrorType.NOT_FOUND_QUESTION));

                    List<QuestionImage> questionImages = questionImageRepository.findAllByQuestion(
                        question);
                    for (QuestionImage questionImage : questionImages) {
                        imageUrls.add(questionImage.getQuestionImageUrl());
                    }
                }
            }


            imagesMap.put(id,imageUrls);
        }

        return imagesMap;
    }


}
