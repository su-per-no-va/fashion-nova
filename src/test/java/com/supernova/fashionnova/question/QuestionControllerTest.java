package com.supernova.fashionnova.question;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.question.dto.QuestionRequestDto;
import com.supernova.fashionnova.question.dto.QuestionResponseDto;
import com.supernova.fashionnova.security.UserDetailsImpl;
import com.supernova.fashionnova.user.User;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private QuestionService service;

    private final String baseUrl = "/questions";
    UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);

    @BeforeEach
    void setUp() {

        // Given a mock UserDetailsImpl
        given(userDetails.getUsername()).willReturn("user");
        given(userDetails.getUser()).willReturn(new User(
            "testUSer",
            "Test1234!@",
            "테스트유저",
            "test@gmail.com",
            "010-1234-5678"
        ));

        // Set the security context
        SecurityContextHolder.setContext(new SecurityContextImpl());
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities()));
    }

    @Test
    @DisplayName("문의 등록 테스트")
    void addQuestion() throws Exception {
        //given
        QuestionRequestDto questionRequestDto = QuestionRequestDto.builder()
            .title("테스트 문의")
            .question("테스트 문의 내용")
            .type("PRODUCT")
            .build();
        doNothing().when(service).addQuestion(any(User.class), any(QuestionRequestDto.class));

        //when * then
        mockMvc.perform(post(baseUrl).with(csrf())
                .content(objectMapper.writeValueAsString(questionRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().string("문의 등록 성공"));

    }

    @Test
    @DisplayName("내 문의 조회 테스트")
    void getAddressList() throws Exception{
        //given
        User user = userDetails.getUser();
        List<QuestionResponseDto> responseDtoList = Arrays.asList(
            new QuestionResponseDto
                (new Question(user, "문의1", "문의내용1", QuestionType.PRODUCT)),
            new QuestionResponseDto
                (new Question(user, "문의2", "문의내용2", QuestionType.DELIVERY))
        );

        //when
        when(service.getUserQuestionList(user)).thenReturn(responseDtoList);

        //then
        mockMvc.perform(get(baseUrl).with(csrf()))
            .andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.length()").value(responseDtoList.size()),
                jsonPath("$[0].title").value("문의1"),
                jsonPath("$[0].question").value("문의내용1"),
                jsonPath("$[0].type").value(QuestionType.PRODUCT.name()),
                jsonPath("$[0].status").value(QuestionStatus.BEFORE.name()),
                jsonPath("$[1].title").value("문의2"),
                jsonPath("$[1].question").value("문의내용2"),
                jsonPath("$[1].type").value(QuestionType.DELIVERY.name()),
                jsonPath("$[1].status").value(QuestionStatus.BEFORE.name())
            );

    }
}