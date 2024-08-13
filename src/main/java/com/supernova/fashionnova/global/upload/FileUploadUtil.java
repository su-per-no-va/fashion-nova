package com.supernova.fashionnova.global.upload;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.supernova.fashionnova.domain.product.Product;
import com.supernova.fashionnova.domain.product.ProductImage;
import com.supernova.fashionnova.domain.product.ProductImageRepository;
import com.supernova.fashionnova.domain.product.ProductRepository;
import com.supernova.fashionnova.domain.question.Question;
import com.supernova.fashionnova.domain.question.QuestionImage;
import com.supernova.fashionnova.domain.question.QuestionImageRepository;
import com.supernova.fashionnova.domain.question.QuestionRepository;
import com.supernova.fashionnova.domain.review.Review;
import com.supernova.fashionnova.domain.review.ReviewImage;
import com.supernova.fashionnova.domain.review.ReviewImageRepository;
import com.supernova.fashionnova.domain.review.ReviewRepository;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import java.io.IOException;
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

        HashMap<String, String> imageUrls = new HashMap<>();

        for (MultipartFile file : files) {

            String originalFilename = file.getOriginalFilename(); // 원본 파일 명
            String s3FileName = random + originalFilename; // 변경된 파일 명 (같은 이름의 파일 방지)

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            try {
                // 파일 업로드
                amazonS3Client.putObject(bucket, s3FileName, file.getInputStream(), metadata);

                imageUrls.put(s3FileName, amazonS3Client.getUrl(bucket, s3FileName).toString());

            } catch (IOException e) {
                throw new CustomException(ErrorType.UPLOAD_REVIEW);
            }

        }

        switch (type) {

            case REVIEW -> {
                Review review = reviewRepository.findById(typeId).orElseThrow(() ->
                    new CustomException(ErrorType.NOT_FOUND_REVIEW));

                for (Map.Entry<String, String> entry : imageUrls.entrySet()) {
                    ReviewImage reviewImage =
                        new ReviewImage(review, entry.getKey(), entry.getValue());
                    reviewImageRepository.save(reviewImage);
                }

            }

            case PRODUCT -> {
                Product product = productRepository.findById(typeId).orElseThrow(() ->
                    new CustomException(ErrorType.NOT_FOUND_PRODUCT));

                for (Map.Entry<String, String> entry : imageUrls.entrySet()) {
                    ProductImage productImage =
                        new ProductImage(product, entry.getKey(), entry.getValue());
                    product.updateImage(productImage.getProductImageUrl());
                    productImageRepository.save(productImage);
                }

            }

            case QUESTION -> {
                Question question = questionRepository.findById(typeId).orElseThrow(() ->
                    new CustomException(ErrorType.NOT_FOUND_QUESTION));

                for (Map.Entry<String, String> entry : imageUrls.entrySet()) {
                    QuestionImage questionImage =
                        new QuestionImage(question, entry.getKey(), entry.getValue());
                    questionImageRepository.save(questionImage);
//                    question.getQuestionImageUrls().add(questionImage);
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
                        // S3에서 이미지 찾아오기
                        String url =
                            amazonS3Client.getResourceUrl(bucket, reviewImage.getFileName());
                        imageUrls.add(url);
                    }

                }

                case PRODUCT -> {
                    Product product = productRepository.findById(id).orElseThrow(
                        () -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));

                    List<ProductImage> productImages =
                        productImageRepository.findAllByProduct(product);

                    for (ProductImage productImage : productImages) {
                        // S3에서 이미지 찾아오기
                        String url =
                            amazonS3Client.getResourceUrl(bucket, productImage.getFileName());
                        imageUrls.add(url);
                    }

                }

                case QUESTION -> {
                    Question question = questionRepository.findById(id).orElseThrow(
                        () -> new CustomException(ErrorType.NOT_FOUND_QUESTION));

                    List<QuestionImage> questionImages =
                        questionImageRepository.findAllByQuestion(question);

                    for (QuestionImage questionImage : questionImages) {
                        // S3에서 이미지 찾아오기
                        String url =
                            amazonS3Client.getResourceUrl(bucket, questionImage.getFileName());
                        imageUrls.add(url);
                    }

                }

            }

            imagesMap.put(id, imageUrls);

        }

        return imagesMap;

    }

    public void deleteImages(ImageType type, Long typeId) {

        switch (type) {
            case REVIEW -> {
                List<ReviewImage> reviewImages = reviewImageRepository.findAllByReviewId(typeId);

                // S3 버킷에서 찾아서 삭제
                for (ReviewImage reviewImage : reviewImages) {
                    amazonS3Client.deleteObject(bucket, reviewImage.getFileName());
                }

            }

            case PRODUCT -> {
                List<ProductImage> productImages =
                    productImageRepository.findAllByProductId(typeId);

                // 상품이미지에서 상품아이디로 삭제
                productImageRepository.deleteAllByProductId(typeId);

                // S3 버킷에서 찾아서 삭제
                for (ProductImage productImage : productImages) {
                    amazonS3Client.deleteObject(bucket, productImage.getFileName());
                }

            }

            case QUESTION -> {
                List<QuestionImage> questionImages = questionImageRepository.findAllByQuestionId(
                    typeId);

                // 상품이미지에서 상품아이디로 삭제
                questionImageRepository.deleteAllByQuestionId(typeId);

                // S3 버킷에서 찾아서 삭제
                for (QuestionImage questionImage : questionImages) {
                    amazonS3Client.deleteObject(bucket, questionImage.getFileName());
                }

            }

        }

    }

}
