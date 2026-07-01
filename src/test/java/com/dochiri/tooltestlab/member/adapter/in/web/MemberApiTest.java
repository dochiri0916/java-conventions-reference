package com.dochiri.tooltestlab.member.adapter.in.web;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberApiTest {

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private static String uniqueEmail() {
        return "test-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    }

    @Test
    @DisplayName("회원 가입에 성공하면 200과 회원 정보를 반환한다")
    void registerSuccess() throws Exception {
        // given
        Map<String, String> body = Map.of(
                "email", uniqueEmail(),
                "password", "password123",
                "name", "홍길동");

        // when
        var result = mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).contains("홍길동");
    }

    @Test
    @DisplayName("중복된 이메일로 가입하면 예외가 발생한다")
    void registerDuplicateEmail() throws Exception {
        // given
        final String email = uniqueEmail();
        Map<String, String> body = Map.of(
                "email", email,
                "password", "password123",
                "name", "홍길동");
        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));

        // when
        var result = mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(409);
    }

    @Test
    @DisplayName("로그인에 성공하면 200과 회원 정보를 반환한다")
    void loginSuccess() throws Exception {
        // given
        final String email = uniqueEmail();
        final String password = "password123";
        Map<String, String> registerBody = Map.of(
                "email", email,
                "password", password,
                "name", "홍길동");
        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerBody)));
        Map<String, String> loginBody = Map.of("email", email, "password", password);

        // when
        var result = mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody)))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).contains(email);
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인하면 실패한다")
    void loginWithWrongPassword() throws Exception {
        // given
        final String email = uniqueEmail();
        Map<String, String> registerBody = Map.of(
                "email", email,
                "password", "password123",
                "name", "홍길동");
        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerBody)));
        Map<String, String> loginBody = Map.of("email", email, "password", "wrongpassword");

        // when
        var result = mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody)))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(401);
    }

    @Test
    @DisplayName("로그인 성공 후 세션으로 내 정보를 조회할 수 있다")
    void meWithSession() throws Exception {
        // given
        final String email = uniqueEmail();
        final String password = "password123";
        Map<String, String> registerBody = Map.of(
                "email", email,
                "password", password,
                "name", "홍길동");
        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerBody)));
        final MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("email", email, "password", password)))
                .session(session));

        // when
        var result = mockMvc.perform(get("/api/members/me").session(session))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).contains("publicId");
    }

    @Test
    @DisplayName("로그인하지 않으면 내 정보 조회 시 401을 반환한다")
    void meWithoutSession() throws Exception {
        // given
        var request = get("/api/members/me");

        // when
        var result = mockMvc.perform(request)
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(401);
    }

    @Test
    @DisplayName("로그아웃 후에는 세션이 무효화된다")
    void logoutInvalidatesSession() throws Exception {
        // given
        final String email = uniqueEmail();
        final String password = "password123";
        Map<String, String> registerBody = Map.of(
                "email", email,
                "password", password,
                "name", "홍길동");
        mockMvc.perform(post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerBody)));
        final MockHttpSession session = new MockHttpSession();
        mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("email", email, "password", password)))
                .session(session));

        // when
        var result = mockMvc.perform(post("/api/members/logout").session(session))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).contains("로그아웃");
    }
}
