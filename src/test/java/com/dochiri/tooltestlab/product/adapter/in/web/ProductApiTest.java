package com.dochiri.tooltestlab.product.adapter.in.web;

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

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ProductApiTest {

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;
    private MockHttpSession session;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        final String email = "product-test-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
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
    }

    @Test
    @DisplayName("상품을 등록할 수 있다")
    void registerProduct() throws Exception {
        Map<String, Object> body = Map.of(
                "name", "맥북 프로 16인치",
                "description", "Apple M4 Pro 칩 탑재",
                "priceAmount", 3_690_000L,
                "stock", 100,
                "categoryIds", List.of());

        mockMvc.perform(post("/api/products")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publicId").isNotEmpty())
                .andExpect(jsonPath("$.name").value("맥북 프로 16인치"))
                .andExpect(jsonPath("$.priceAmount").value(3_690_000))
                .andExpect(jsonPath("$.stock").value(100));
    }

    @Test
    @DisplayName("상품 목록을 페이지네이션으로 조회할 수 있다")
    void listProducts() throws Exception {
        for (int i = 0; i < 3; i++) {
            Map<String, Object> body = Map.of(
                    "name", "상품" + i,
                    "description", "설명" + i,
                    "priceAmount", 10000L * (i + 1),
                    "stock", 10,
                    "categoryIds", List.of());
            mockMvc.perform(post("/api/products")
                    .session(session)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body)));
        }

        mockMvc.perform(get("/api/products")
                        .session(session)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(greaterThanOrEqualTo(3))))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    @DisplayName("상품 상세 정보를 publicId로 조회할 수 있다")
    void getProduct() throws Exception {
        Map<String, Object> body = Map.of(
                "name", "맥북 프로",
                "description", "고성능 노트북",
                "priceAmount", 3_690_000L,
                "stock", 100,
                "categoryIds", List.of());

        String response = mockMvc.perform(post("/api/products")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String publicId = objectMapper.readTree(response).get("publicId").asText();

        mockMvc.perform(get("/api/products/" + publicId).session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publicId").value(publicId))
                .andExpect(jsonPath("$.name").value("맥북 프로"))
                .andExpect(jsonPath("$.description").value("고성능 노트북"))
                .andExpect(jsonPath("$.priceAmount").value(3_690_000))
                .andExpect(jsonPath("$.stock").value(100));
    }

    @Test
    @DisplayName("존재하지 않는 상품을 조회하면 404를 반환한다")
    void getProductNotFound() throws Exception {
        mockMvc.perform(get("/api/products/nonexistent-id").session(session))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("인증되지 않은 사용자는 상품을 등록할 수 없다")
    void registerProductWithoutAuth() throws Exception {
        Map<String, Object> body = Map.of(
                "name", "맥북 프로",
                "description", "고성능 노트북",
                "priceAmount", 3_690_000L,
                "stock", 100,
                "categoryIds", List.of());

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isUnauthorized());
    }
}
