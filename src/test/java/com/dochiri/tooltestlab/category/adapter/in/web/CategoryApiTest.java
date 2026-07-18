package com.dochiri.tooltestlab.category.adapter.in.web;

import com.dochiri.tooltestlab.catalog.category.adapter.in.web.response.CategoryListResponse;
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

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
class CategoryApiTest {

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;
    private MockHttpSession session;

    @BeforeEach
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        final String email = "category-test-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
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
    @DisplayName("카테고리를 등록할 수 있다")
    void registerCategory() throws Exception {
        // given
        final String categoryName = "전자기기-" + UUID.randomUUID().toString().substring(0, 8);
        Map<String, String> body = Map.of("name", categoryName);

        // when
        var result = mockMvc.perform(post("/api/categories")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).contains(categoryName);
    }

    @Test
    @DisplayName("동일한 카테고리명을 등록하면 409 오류 계약을 반환한다")
    void registerDuplicateCategoryName() throws Exception {
        // given
        final String categoryName = "중복 카테고리-" + UUID.randomUUID().toString().substring(0, 8);
        Map<String, String> body = Map.of("name", categoryName);
        mockMvc.perform(post("/api/categories")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));

        // when
        var result = mockMvc.perform(post("/api/categories")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(409);
        assertThat(result.getResponse().getContentAsString()).contains(
                "CATEGORY.DUPLICATE_CATEGORY_NAME", "카테고리명 중복", "이미 등록된 카테고리명입니다.");
    }

    @Test
    @DisplayName("카테고리 목록을 조회할 수 있다")
    void listCategories() throws Exception {
        // given
        Map<String, String> body = Map.of("name", "전자기기");
        mockMvc.perform(post("/api/categories")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));

        // when
        var result = mockMvc.perform(get("/api/categories").session(session))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).startsWith("[");
    }

    @Test
    @DisplayName("인증되지 않은 사용자는 카테고리를 등록할 수 없다")
    void registerCategoryWithoutAuth() throws Exception {
        // given
        Map<String, String> body = Map.of("name", "전자기기");

        // when
        var result = mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(401);
    }

    @Test
    @DisplayName("CategoryResponse 레코드를 생성할 수 있다")
    void categoryResponseCreation() {
        // given
        var publicId = "cat123test";
        var name = "전자기기";

        // when
        var response = new CategoryListResponse(
                publicId, name);

        // then
        assertThat(response.publicId()).isEqualTo(publicId);
        assertThat(response.name()).isEqualTo(name);
    }
}
