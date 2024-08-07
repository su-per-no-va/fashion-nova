package com.supernova.fashionnova.question;


import com.supernova.fashionnova.domain.question.QuestionRepository;
import com.supernova.fashionnova.domain.question.QuestionService;
import com.supernova.fashionnova.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    /*
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
        // given
        Question question1 = Mockito.mock(Question.class);
        Question question2 = Mockito.mock(Question.class);

        List<Question> questionList = List.of(question1, question2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Question> questionPage = new PageImpl<>(questionList, pageable, questionList.size());

        given(questionRepository.findByUser(user, pageable)).willReturn(questionPage);

        // when
        List<QuestionResponseDto> savedQuestionPage = questionService.getUserQuestionList(user, 0);

        // then
        assertThat(savedQuestionPage).isNotNull();

        QuestionResponseDto responseDto1 = savedQuestionPage.get(0);
        assertThat(responseDto1.getTitle()).isEqualTo("문의1");
        assertThat(responseDto1.getQuestion()).isEqualTo("문의 내용1");
        assertThat(responseDto1.getType()).isEqualTo(QuestionType.PRODUCT);
        assertThat(responseDto1.getStatus()).isEqualTo(QuestionStatus.BEFORE);

        QuestionResponseDto responseDto2 = savedQuestionPage.get(1);
        assertThat(responseDto2.getTitle()).isEqualTo("문의2");
        assertThat(responseDto2.getQuestion()).isEqualTo("문의 내용2");
        assertThat(responseDto2.getType()).isEqualTo(QuestionType.DELIVERY);
        assertThat(responseDto2.getStatus()).isEqualTo(QuestionStatus.BEFORE);
    }

     */

}
