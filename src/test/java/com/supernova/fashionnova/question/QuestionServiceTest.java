package com.supernova.fashionnova.question;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

import com.supernova.fashionnova.question.dto.QuestionRequestDto;
import com.supernova.fashionnova.question.dto.QuestionResponseDto;
import com.supernova.fashionnova.user.User;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    private User user;

    @InjectMocks
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        this.user = User.builder()
            .userName("testUser1234")
            .name("테스트유저")
            .password("test1234!#")
            .email("test@gmail.com")
            .phone("010-1234-5678")
            .build();
    }


    @Test
    @DisplayName("문의 생성 테스트")
    void addQuestionTest() {
        //given
       QuestionRequestDto questionRequestDto = QuestionRequestDto.builder()
            .title("테스트 문의")
            .question("테스트 문의 내용")
            .type("PRODUCT")
            .build();

        //when * then
        assertDoesNotThrow(()-> questionService.addQuestion(user,questionRequestDto,null));

    }

    @Test
    @DisplayName("내 문의 조회 테스트")
    void getUserQuestionPageTest() {
        //given
        Question question1 = Question.builder()
            .user(user)
            .title("문의1")
            .question("문의 내용1")
            .type(QuestionType.valueOf("PRODUCT"))
            .build();

        Question question2 = Question.builder()
            .user(user)
            .title("문의2")
            .question("문의 내용2")
            .type(QuestionType.valueOf("DELIVERY"))
            .build();

        List<Question> questionList = List.of(question1, question2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Question> questionPage = new PageImpl<>(questionList);

        given(questionRepository.findByUser(user, pageable)).willReturn(questionPage);

        // when
        List<QuestionResponseDto> savedQuestionPage = questionService.getUserQuestionList(user, 1);


        // then
        assertThat(savedQuestionPage).isNotNull();
        assertThat(savedQuestionPage).hasSize(2);

        QuestionResponseDto responseDto1 = savedQuestionPage.get(0);
        assertThat(responseDto1.getTitle()).isEqualTo("문의1");
        assertThat(responseDto1.getQuestion()).isEqualTo("문의 내용1");
        assertThat(responseDto1.getType()).isEqualTo(QuestionType.PRODUCT);
        assertThat(responseDto1.getStatus()).isEqualTo(QuestionStatus.BEFORE);

        QuestionResponseDto responseDto2 = savedQuestionPage.get(1);
        assertThat(responseDto2.getTitle()).isEqualTo("문의2");
        assertThat(responseDto2.getType()).isEqualTo(QuestionType.DELIVERY);
        assertThat(responseDto2.getStatus()).isEqualTo(QuestionStatus.BEFORE);
        assertThat(responseDto2.getQuestion()).isEqualTo("문의 내용2");
    }
}