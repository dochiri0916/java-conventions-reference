package com.dochiri.tooltestlab.review.adapter.in.web;

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
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ReviewApiTest {

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;
    private MockHttpSession session;
    private String productPublicId;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        final String email = "review-test-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
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
                "name", "리뷰 테스트 상품",
                "description", "리뷰 테스트용",
                "priceAmount", 50_000L,
                "stock", 50,
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
    @DisplayName("리뷰를 작성할 수 있다")
    void writeReview() throws Exception {
        Map<String, Object> body = Map.of(
                "productPublicId", productPublicId,
                "rating", 5,
                "content", "정말 좋은 제품입니다!");

        mockMvc.perform(post("/api/reviews")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publicId").isNotEmpty())
                .andExpect(jsonPath("$.message").value(containsString("등록")));
    }

    @Test
    @DisplayName("상품의 리뷰 목록을 인증 없이 조회할 수 있다")
    void getProductReviewsWithoutAuth() throws Exception {
        Map<String, Object> body = Map.of(
                "productPublicId", productPublicId,
                "rating", 4,
                "content", "좋아요");
        mockMvc.perform(post("/api/reviews")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));

        mockMvc.perform(get("/api/reviews/product/" + productPublicId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productPublicId").value(productPublicId))
                .andExpect(jsonPath("$.reviews", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.averageRating").isNumber());
    }

    @Test
    @DisplayName("리뷰를 삭제할 수 있다")
    void deleteReview() throws Exception {
        Map<String, Object> body = Map.of(
                "productPublicId", productPublicId,
                "rating", 3,
                "content", "보통이에요");
        String response = mockMvc.perform(post("/api/reviews")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        final String reviewPublicId = objectMapper.readTree(response).get("publicId").asText();

        mockMvc.perform(delete("/api/reviews/" + reviewPublicId).session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("삭제")));
    }

    @Test
    @DisplayName("인증되지 않은 사용자는 리뷰를 작성할 수 없다")
    void writeReviewWithoutAuth() throws Exception {
        Map<String, Object> body = Map.of(
                "productPublicId", productPublicId,
                "rating", 5,
                "content", "리뷰 내용");

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isUnauthorized());
    }
}
