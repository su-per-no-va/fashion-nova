package com.supernova.fashionnova.question;

import com.supernova.fashionnova.question.dto.QuestionRequestDto;
import com.supernova.fashionnova.question.dto.QuestionResponseDto;
import com.supernova.fashionnova.user.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    /**
     * 문의 등록
     *
     * @param user
     * @param requestDto
     */
    public void addQuestion(User user, QuestionRequestDto requestDto) {

        Question question = Question.builder()
            .user(user)
            .title(requestDto.getTitle())
            .question(requestDto.getQuestion())
            .type(QuestionType.valueOf(requestDto.getType()))
            .build();

        questionRepository.save(question);
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
