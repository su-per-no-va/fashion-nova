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
import com.supernova.fashionnova.warn.Warn;
import com.supernova.fashionnova.warn.dto.WarnRepository;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
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
    public void uploadReviewImage(MultipartFile file, ImageType type, Long typeId) {

        String originalFilename = file.getOriginalFilename(); // 원본 파일 명
        String s3FileName = random + originalFilename; // 변경된 파일 명 (같은 이름의 파일 방지)

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        String reviewImageURL;
        try {
            amazonS3Client.putObject(bucket, s3FileName, file.getInputStream(), metadata); // 파일 업로드

            reviewImageURL = amazonS3Client.getUrl(bucket, s3FileName).toString();
        } catch (IOException e) {
            throw new CustomException(ErrorType.UPLOAD_REVIEW);
        }

       switch (type){
           case REVIEW -> {
               Review review = reviewRepository.findById(typeId).orElseThrow(() ->
                   new CustomException(ErrorType.NOT_FOUND_REVIEW));

               ReviewImage reviewImage = new ReviewImage(review, reviewImageURL);
               reviewImageRepository.save(reviewImage);
           }

           case PRODUCT -> {
               Product product = productRepository.findById(typeId).orElseThrow(() ->
                   new CustomException(ErrorType.NOT_FOUND_PRODUCT));

               ProductImage productImage = new ProductImage(product, reviewImageURL);
               productImageRepository.save(productImage);
           }

           case QUESTION -> {
               Question question = questionRepository.findById(typeId).orElseThrow(() ->
                   new CustomException(ErrorType.NOT_FOUND_QUESTION));

               QuestionImage questionImage = new QuestionImage(question, reviewImageURL);
               questionImageRepository.save(questionImage);
               나 류정근인데 나 바보맞다 ㅋㅋㅋㅋㅋㅋ
           }//야 나 류정근인데 가슴털많다 나
       }
//
    }
}
