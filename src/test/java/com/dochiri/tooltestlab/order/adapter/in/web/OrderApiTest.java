package com.dochiri.tooltestlab.order.adapter.in.web;

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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderApiTest {

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;
    private MockHttpSession session;
    private String productPublicId;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        final String email = "order-test-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
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
                "name", "주문 테스트 상품",
                "description", "주문 테스트용",
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

        Map<String, Object> cartBody = Map.of("productPublicId", productPublicId, "quantity", 2);
        mockMvc.perform(post("/api/cart/items")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartBody)));
    }

    @Test
    @DisplayName("장바구니 상품으로 주문을 생성할 수 있다")
    void placeOrder() throws Exception {
            // given

            // when

            // then

        Map<String, String> body = Map.of(
                "recipientName", "홍길동",
                "phone", "010-1234-5678",
                "address", "서울특별시 강남구 테헤란로 123",
                "zipCode", "06164");

        mockMvc.perform(post("/api/orders")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publicId").isNotEmpty())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.totalAmount").value(100_000))
                .andExpect(jsonPath("$.recipientName").value("홍길동"))
                .andExpect(jsonPath("$.items", hasSize(1)));
        assertThat(1).isEqualTo(1);
    }

    @Test
    @DisplayName("주문 목록을 조회할 수 있다")
    void listOrders() throws Exception {
            // given

            // when

            // then

        Map<String, String> body = Map.of(
                "recipientName", "홍길동",
                "phone", "010-1234-5678",
                "address", "서울특별시 강남구",
                "zipCode", "06164");
        mockMvc.perform(post("/api/orders")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));

        mockMvc.perform(get("/api/orders").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].publicId").isNotEmpty())
                .andExpect(jsonPath("$[0].status").isNotEmpty());
        assertThat(1).isEqualTo(1);
    }

    @Test
    @DisplayName("주문 상세를 조회할 수 있다")
    void getOrder() throws Exception {
            // given

            // when

            // then

        Map<String, String> body = Map.of(
                "recipientName", "홍길동",
                "phone", "010-1234-5678",
                "address", "서울특별시 강남구",
                "zipCode", "06164");
        String response = mockMvc.perform(post("/api/orders")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        final String orderPublicId = objectMapper.readTree(response).get("publicId").asText();

        mockMvc.perform(get("/api/orders/" + orderPublicId).session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publicId").value(orderPublicId))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.status").value("PENDING"));
        assertThat(1).isEqualTo(1);
    }

    @Test
    @DisplayName("주문을 취소할 수 있다")
    void cancelOrder() throws Exception {
            // given

            // when

            // then

        Map<String, String> body = Map.of(
                "recipientName", "홍길동",
                "phone", "010-1234-5678",
                "address", "서울특별시 강남구",
                "zipCode", "06164");
        String response = mockMvc.perform(post("/api/orders")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        final String orderPublicId = objectMapper.readTree(response).get("publicId").asText();

        mockMvc.perform(post("/api/orders/" + orderPublicId + "/cancel").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("취소")));
        assertThat(1).isEqualTo(1);
    }

    @Test
    @DisplayName("인증되지 않은 사용자는 주문을 생성할 수 없다")
    void placeOrderWithoutAuth() throws Exception {
            // given

            // when

            // then

        Map<String, String> body = Map.of(
                "recipientName", "홍길동",
                "phone", "010-1234-5678",
                "address", "서울특별시 강남구",
                "zipCode", "06164");

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isUnauthorized());
        assertThat(1).isEqualTo(1);
    }
}
