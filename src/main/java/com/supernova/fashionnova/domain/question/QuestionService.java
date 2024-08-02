package com.supernova.fashionnova.domain.question;

import com.amazonaws.util.CollectionUtils;
import com.supernova.fashionnova.domain.question.dto.QuestionRequestDto;
import com.supernova.fashionnova.domain.question.dto.QuestionResponseDto;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.global.upload.FileUploadUtil;
import com.supernova.fashionnova.global.upload.ImageType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    private final FileUploadUtil fileUploadUtil;

    /**
     * 문의 등록
     *
     * @param user
     * @param requestDto
     */
    public void addQuestion(User user, QuestionRequestDto requestDto, List<MultipartFile> files) {

        Question question = Question.builder()
            .user(user)
            .title(requestDto.getTitle())
            .question(requestDto.getQuestion())
            .type(QuestionType.valueOf(requestDto.getType()))
            .build();

        questionRepository.save(question);
        //파일 업로드
        if (!CollectionUtils.isNullOrEmpty(files)) {
            fileUploadUtil.uploadImage(files, ImageType.QUESTION, question.getId());
        }

    }

    /**
     * 내 문의 조회
     *
     * @param user
     * @param page
     * @return List<QuestionResponseDto>
     */
    public List<QuestionResponseDto> getUserQuestionList(User user, int page) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Question> questionPage = questionRepository.findByUser(user, pageable);

        return questionPage.stream()
            .map(QuestionResponseDto::new)
            .collect(Collectors.toList());

    }

}