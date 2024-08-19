/*
package com.supernova.fashionnova.product;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.domain.product.Product;
import com.supernova.fashionnova.domain.product.ProductController;
import com.supernova.fashionnova.domain.product.ProductService;
import com.supernova.fashionnova.domain.product.dto.ProductResponseDto;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String baseUrl = "/products";
    UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);

    @MockBean
    private ProductService productService;

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
    @DisplayName("상품 조회 테스트")
    void getProductListTest() throws Exception {
        // given
        int page = 0;

        Product product1 = Mockito.mock(Product.class);
        Product product2 = Mockito.mock(Product.class);

        List<Product> products = new ArrayList<>(Arrays.asList(product1, product2));
        Page<Product> productPage = new PageImpl<>(products);
        Page<ProductResponseDto> responseDto = productPage.map(ProductResponseDto::new);
        when(productService.getProductList(null, null, null, null, null, page))
            .thenReturn(responseDto);

        // when * then
        mockMvc.perform(get(baseUrl + "/product")
                .param("sort", "")
                .param("category", "")
                .param("size", "")
                .param("color", "")
                .param("page", String.valueOf(page))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().isOk()
            );

    }

}

 */