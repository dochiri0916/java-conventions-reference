package com.dochiri.tooltestlab.cart.adapter.in.web;

import com.dochiri.tooltestlab.cart.application.exception.CartCreationConflictException;
import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;
import com.dochiri.tooltestlab.global.exception.GlobalApplicationExceptionHandler;
import com.dochiri.tooltestlab.global.exception.ProblemDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CartExceptionHandlerTest {

    private final ProblemDetails problemDetails = new ProblemDetails(Clock.systemUTC());
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
            .setControllerAdvice(new CartExceptionHandler(problemDetails),
                    new GlobalApplicationExceptionHandler(problemDetails))
            .build();

    @Test
    @DisplayName("장바구니 동시 생성 충돌은 409로 전달하고 내부 원인을 노출하지 않는다")
    void handles_cart_creation_conflict() throws Exception {
        // given
        final String path = "/test/cart/conflict";

        // when
        final var result = mockMvc.perform(get(path));

        // then
        result.andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("CART_CREATION_CONFLICT"))
                .andExpect(jsonPath("$.detail").value(
                        "장바구니가 동시에 생성되어 요청을 완료하지 못했습니다. 다시 시도해주세요."));
    }

    @Test
    @DisplayName("다른 저장소 오류는 기존 500 응답을 유지한다")
    void retains_repository_failure_handling() throws Exception {
        // given
        final String path = "/test/cart/repository";

        // when
        final var result = mockMvc.perform(get(path));

        // then
        result.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.detail").value("일시적인 오류가 발생했습니다."));
    }

    @RestController
    static class TestController {

        @GetMapping("/test/cart/conflict")
        public void conflict() {
            throw new CartCreationConflictException(new IllegalStateException("internal constraint information"));
        }

        @GetMapping("/test/cart/repository")
        public void repository() {
            throw new RepositoryOperationException(new IllegalStateException("internal storage failure"));
        }
    }
}
