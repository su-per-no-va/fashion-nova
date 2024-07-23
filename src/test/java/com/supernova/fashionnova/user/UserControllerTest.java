package com.supernova.fashionnova.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.security.UserDetailsImpl;
import com.supernova.fashionnova.user.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * create on 2024/07/23 create by IntelliJ IDEA.
 *
 * <p> New Project. </p>
 *
 * @author HoChan Son (hcson)
 * @version 1.0
 * @since 1.0
 */
@WebMvcTest(UserController.class)
class UserControllerTest {


  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private UserService service;

  private final String baseUrl = "/users";

  @BeforeEach
  void setUp() {
    // Given a mock UserDetailsImpl
    UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);
    given(userDetails.getUsername()).willReturn("user");
    given(userDetails.getUser()).willReturn(new User());

    // Set the security context
    SecurityContextHolder.setContext(new SecurityContextImpl());
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
  }


  @Test
  void getUserTest() throws Exception {
    // given
    UserResponseDto userResponseDto = Mockito.mock(UserResponseDto.class);
    given(userResponseDto.getUserName()).willReturn("usernmame");
    given(userResponseDto.getName()).willReturn("name");
    given(userResponseDto.getEmail()).willReturn("email");

    // when
    when(service.getUser(any())).thenReturn(userResponseDto);


    mockMvc.perform(get(baseUrl)
        )
        .andExpectAll(status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.userName").exists(),
            jsonPath("$.name").exists(),
            jsonPath("$.email").exists()
        );

  }
}