package com.supernova.fashionnova.question;

import com.supernova.fashionnova.question.dto.QuestionRequestDto;
import com.supernova.fashionnova.question.dto.QuestionResponseDto;
import com.supernova.fashionnova.upload.FileUploadUtil;
import com.supernova.fashionnova.upload.ImageType;
import com.supernova.fashionnova.user.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
    public void addQuestion(User user, QuestionRequestDto requestDto, MultipartFile file) {

        Question question = Question.builder()
            .user(user)
            .title(requestDto.getTitle())
            .question(requestDto.getQuestion())
            .type(QuestionType.valueOf(requestDto.getType()))
            .build();

        fileUploadUtil.uploadImage(file, ImageType.QUESTION,question.getId());

        questionRepository.save(question);
    }

    /**
     * 내 문의 조회
     *
     * @param user
     * @return List<QuestionResponseDto>
     */
    public List<QuestionResponseDto> getUserQuestionList(User user) {

        List<Question> addresses = questionRepository.findByUser(user);

        return addresses.stream()
            .map(QuestionResponseDto::new)
            .collect(Collectors.toList());
    }

}
