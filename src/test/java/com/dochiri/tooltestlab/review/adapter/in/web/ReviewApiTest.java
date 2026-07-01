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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
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
        // given
        Map<String, Object> body = Map.of(
                "productPublicId", productPublicId,
                "rating", 5,
                "content", "정말 좋은 제품입니다!");

        // when
        var result = mockMvc.perform(post("/api/reviews")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        var json = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(json.get("publicId").asText()).isNotEmpty();
        assertThat(result.getResponse().getContentAsString()).contains("등록");
    }

    @Test
    @DisplayName("상품의 리뷰 목록을 인증 없이 조회할 수 있다")
    void retrieveProductReviewsWithoutAuth() throws Exception {
        // given
        Map<String, Object> body = Map.of(
                "productPublicId", productPublicId,
                "rating", 4,
                "content", "좋아요");
        mockMvc.perform(post("/api/reviews")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));

        // when
        var result = mockMvc.perform(get("/api/reviews/product/" + productPublicId))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        var json = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(json.get("productPublicId").asText()).isEqualTo(productPublicId);
        assertThat(json.get("reviews")).hasSizeGreaterThanOrEqualTo(1);
        assertThat(json.get("averageRating").isNumber()).isTrue();
    }

    @Test
    @DisplayName("리뷰를 삭제할 수 있다")
    void deleteReview() throws Exception {
        // given
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

        // when
        var result = mockMvc.perform(delete("/api/reviews/" + reviewPublicId).session(session))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).contains("삭제");
    }

    @Test
    @DisplayName("인증되지 않은 사용자는 리뷰를 작성할 수 없다")
    void writeReviewWithoutAuth() throws Exception {
        // given
        Map<String, Object> body = Map.of(
                "productPublicId", productPublicId,
                "rating", 5,
                "content", "리뷰 내용");

        // when
        var result = mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(401);
    }
}
