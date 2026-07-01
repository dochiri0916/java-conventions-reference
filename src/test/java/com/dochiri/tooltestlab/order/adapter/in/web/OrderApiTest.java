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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class OrderApiTest {

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;
    private MockHttpSession session;

    @BeforeEach
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
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
        final String productPublicId = objectMapper.readTree(productResponse).get("publicId").asText();

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
        Map<String, String> body = Map.of(
                "recipientName", "홍길동",
                "phone", "010-1234-5678",
                "address", "서울특별시 강남구 테헤란로 123",
                "zipCode", "06164");

        // when
        var result = mockMvc.perform(post("/api/orders")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        var json = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(json.get("publicId").asText()).isNotEmpty();
        assertThat(json.get("status").asText()).isEqualTo("PENDING");
        assertThat(json.get("totalAmount").asInt()).isEqualTo(100_000);
        assertThat(json.get("recipientName").asText()).isEqualTo("홍길동");
        assertThat(json.get("items")).hasSize(1);
    }

    @Test
    @DisplayName("주문 목록을 조회할 수 있다")
    void listOrders() throws Exception {
        // given
        Map<String, String> body = Map.of(
                "recipientName", "홍길동",
                "phone", "010-1234-5678",
                "address", "서울특별시 강남구",
                "zipCode", "06164");
        mockMvc.perform(post("/api/orders")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));

        // when
        var result = mockMvc.perform(get("/api/orders").session(session))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        var json = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(json).hasSizeGreaterThanOrEqualTo(1);
        assertThat(json.get(0).get("publicId").asText()).isNotEmpty();
        assertThat(json.get(0).get("status").asText()).isNotEmpty();
    }

    @Test
    @DisplayName("주문 상세를 조회할 수 있다")
    void retrieveOrder() throws Exception {
        // given
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

        // when
        var result = mockMvc.perform(get("/api/orders/" + orderPublicId).session(session))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        var json = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(json.get("publicId").asText()).isEqualTo(orderPublicId);
        assertThat(json.get("items")).hasSize(1);
        assertThat(json.get("status").asText()).isEqualTo("PENDING");
    }

    @Test
    @DisplayName("주문을 취소할 수 있다")
    void cancelOrder() throws Exception {
        // given
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

        // when
        var result = mockMvc.perform(post("/api/orders/" + orderPublicId + "/cancel").session(session))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).contains("취소");
    }

    @Test
    @DisplayName("인증되지 않은 사용자는 주문을 생성할 수 없다")
    void placeOrderWithoutAuth() throws Exception {
        // given
        Map<String, String> body = Map.of(
                "recipientName", "홍길동",
                "phone", "010-1234-5678",
                "address", "서울특별시 강남구",
                "zipCode", "06164");

        // when
        var result = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(401);
    }
}
