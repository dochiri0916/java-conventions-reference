package com.dochiri.tooltestlab.cart.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CartApiTest {

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;
    private MockHttpSession session;
    private String productPublicId;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        final String email = "cart-test-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        Map<String, String> registerBody = Map.of(
                "email", email,
                "password", "password123",
                "name", "테스터");

        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerBody)));

        session = new MockHttpSession();
        mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("email", email, "password", "password123")))
                .session(session));

        Map<String, Object> productBody = Map.of(
                "name", "테스트 상품",
                "description", "장바구니 테스트용",
                "priceAmount", 10_000L,
                "stock", 100,
                "categoryIds", List.of());
        String productResponse = mockMvc.perform(post("/api/products")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productBody)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        productPublicId = objectMapper.readTree(productResponse).get("publicId").asText();
    }

    @Test
    @DisplayName("장바구니에 상품을 추가할 수 있다")
    void addToCart() throws Exception {
        Map<String, Object> body = Map.of("productPublicId", productPublicId, "quantity", 2);

        mockMvc.perform(post("/api/cart/items")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("추가")));
    }

    @Test
    @DisplayName("장바구니를 조회할 수 있다")
    void getCart() throws Exception {
        Map<String, Object> body = Map.of("productPublicId", productPublicId, "quantity", 3);
        mockMvc.perform(post("/api/cart/items")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));

        mockMvc.perform(get("/api/cart").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publicId").isNotEmpty())
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].productPublicId").value(productPublicId))
                .andExpect(jsonPath("$.items[0].quantity").value(3))
                .andExpect(jsonPath("$.totalAmount").isNumber());
    }

    @Test
    @DisplayName("장바구니에 동일 상품을 추가하면 수량이 증가한다")
    void addSameProductIncreasesQuantity() throws Exception {
        Map<String, Object> body = Map.of("productPublicId", productPublicId, "quantity", 2);
        mockMvc.perform(post("/api/cart/items")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));
        mockMvc.perform(post("/api/cart/items")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));

        mockMvc.perform(get("/api/cart").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].quantity").value(4));
    }

    @Test
    @DisplayName("장바구니에서 상품을 제거할 수 있다")
    void removeFromCart() throws Exception {
        Map<String, Object> body = Map.of("productPublicId", productPublicId, "quantity", 2);
        mockMvc.perform(post("/api/cart/items")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));

        mockMvc.perform(delete("/api/cart/items/" + productPublicId).session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("제거")));
    }

    @Test
    @DisplayName("수량이 0 이하이면 400 검증 오류를 반환한다")
    void addToCartWithInvalidQuantity() throws Exception {
        Map<String, Object> body = Map.of("productPublicId", productPublicId, "quantity", 0);

        mockMvc.perform(post("/api/cart/items")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("인증되지 않은 사용자는 장바구니에 접근할 수 없다")
    void cartWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isUnauthorized());
    }
}
