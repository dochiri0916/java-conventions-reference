package com.dochiri.tooltestlab.order.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ProblemType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class OrderErrorCodeMappingProviderTest {

    @Test
    @DisplayName("주문 에러 코드 17개를 모두 매핑한다")
    void hasAllMappings() {
        // given
        var provider = new OrderErrorCodeMappingProvider();

        // when
        Map<String, ApiExceptionMapper.Mapping> mappings = provider.errorCodeMappings();

        // then
        assertThat(mappings).isNotNull().hasSize(17);
    }

    @Test
    @DisplayName("모든 매핑이 non-null status와 problemType을 가진다")
    void allMappingsHaveValidStatusAndProblemType() {
        // given
        var provider = new OrderErrorCodeMappingProvider();
        Map<String, ApiExceptionMapper.Mapping> mappings = provider.errorCodeMappings();

        // when & then
        assertThat(mappings.values()).allSatisfy(mapping -> {
            assertThat(mapping.status()).isNotNull();
            assertThat(mapping.problemType()).isNotNull();
        });
    }

    @Test
    @DisplayName("ORDER_NOT_FOUND는 404 NOT_FOUND로 매핑한다")
    void orderNotFound() {
        // given
        var provider = new OrderErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("ORDER_NOT_FOUND");

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.NOT_FOUND);
    }

    @Test
    @DisplayName("NOT_ORDER_OWNER는 403 FORBIDDEN으로 매핑한다")
    void notOrderOwner() {
        // given
        var provider = new OrderErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("NOT_ORDER_OWNER");

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.FORBIDDEN);
    }

    @Test
    @DisplayName("BAD_ORDER_STATUS는 400 BUSINESS_RULE_VIOLATION으로 매핑한다")
    void badOrderStatus() {
        // given
        var provider = new OrderErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("BAD_ORDER_STATUS");

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.BUSINESS_RULE_VIOLATION);
    }

    @Test
    @DisplayName("EMPTY_CART는 400 BUSINESS_RULE_VIOLATION으로 매핑한다")
    void emptyCart() {
        // given
        var provider = new OrderErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("EMPTY_CART");

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.BUSINESS_RULE_VIOLATION);
    }

    @Test
    @DisplayName("ORDER_ID_REQUIRED는 400 VALIDATION_FAILED로 매핑한다")
    void orderIdRequired() {
        // given
        var provider = new OrderErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("ORDER_ID_REQUIRED");

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }
}
