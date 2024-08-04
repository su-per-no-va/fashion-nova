package com.supernova.fashionnova.wish;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.domain.wish.WishController;
import com.supernova.fashionnova.domain.wish.WishService;
import com.supernova.fashionnova.domain.wish.dto.WishDeleteRequestDto;
import com.supernova.fashionnova.domain.wish.dto.WishRequestDto;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
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

@WebMvcTest(WishController.class)
class WishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String baseUrl = "/wishlists";
    UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);

    @MockBean
    private WishService wishService;

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
    void addWishTest() throws Exception {

        // given
        User user = userDetails.getUser();
        WishRequestDto requestDto = new WishRequestDto(1L);
        doNothing().when(wishService).addWish(eq(user), eq(requestDto));

        // when * then
        mockMvc.perform(post(baseUrl)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpectAll(
                status().isCreated(),
                content().string("위시리스트 추가")
            );

    }

    /*
    @Test
    void getWishProductListTest() throws Exception {
        // given
        User user = userDetails.getUser();
        int page = 1;

        Product product1 = Mockito.mock(Product.class);
        Product product2 = Mockito.mock(Product.class);

        List<Product> products = new ArrayList<>(Arrays.asList(product1, product2));
        Page<Product> productPage = new PageImpl<>(products);
        Page<ProductResponseDto> responseDto = productPage.map(ProductResponseDto::new);

        when(wishService.getWishProductList(user, 0)).thenReturn(responseDto.getContent());

        // when * then
        mockMvc.perform(get(baseUrl).param("page", String.valueOf(page))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().isOk()
            );

    }
    */

    @Test
    void deleteWishTest() throws Exception {

        // given
        User user = userDetails.getUser();
        WishDeleteRequestDto requestDto = new WishDeleteRequestDto(1L);
        doNothing().when(wishService).deleteWish(eq(user), eq(requestDto));

        // when * then
        mockMvc.perform(delete(baseUrl)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpectAll(
                status().isOk(),
                content().string("위시리스트 삭제")
            );

    }

}
