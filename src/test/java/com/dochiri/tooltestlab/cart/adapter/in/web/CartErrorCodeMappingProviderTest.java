package com.dochiri.tooltestlab.cart.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ProblemType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CartErrorCodeMappingProviderTest {

    @Test
    @DisplayName("장바구니 에러 코드 11개를 모두 매핑한다")
    void hasAllMappings() {
        // given
        var provider = new CartErrorCodeMappingProvider();

        // when
        Map<String, ApiExceptionMapper.Mapping> mappings = provider.errorCodeMappings();

        // then
        assertThat(mappings).isNotNull().hasSize(11);
    }

    @Test
    @DisplayName("모든 매핑이 non-null status와 problemType을 가진다")
    void allMappingsHaveValidStatusAndProblemType() {
        // given
        var provider = new CartErrorCodeMappingProvider();
        Map<String, ApiExceptionMapper.Mapping> mappings = provider.errorCodeMappings();

        // when & then
        assertThat(mappings.values()).allSatisfy(mapping -> {
            assertThat(mapping.status()).isNotNull();
            assertThat(mapping.problemType()).isNotNull();
        });
    }

    @Test
    @DisplayName("CART_NOT_FOUND는 404 NOT_FOUND로 매핑한다")
    void cartNotFound() {
        // given
        var provider = new CartErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("CART_NOT_FOUND");

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.NOT_FOUND);
    }

    @Test
    @DisplayName("CART_ITEM_NOT_FOUND는 404 NOT_FOUND로 매핑한다")
    void cartItemNotFound() {
        // given
        var provider = new CartErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("CART_ITEM_NOT_FOUND");

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.NOT_FOUND);
    }

    @Test
    @DisplayName("NOT_CART_OWNER는 403 FORBIDDEN으로 매핑한다")
    void notCartOwner() {
        // given
        var provider = new CartErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("NOT_CART_OWNER");

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.FORBIDDEN);
    }

    @Test
    @DisplayName("CART_ID_REQUIRED는 400 VALIDATION_FAILED로 매핑한다")
    void cartIdRequired() {
        // given
        var provider = new CartErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("CART_ID_REQUIRED");

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }

    @Test
    @DisplayName("TOO_MANY_ITEMS는 400 VALIDATION_FAILED로 매핑한다")
    void tooManyItems() {
        // given
        var provider = new CartErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("TOO_MANY_ITEMS");

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }

    @Test
    @DisplayName("INVALID_INPUT은 400 VALIDATION_FAILED로 매핑한다")
    void invalidInput() {
        // given
        var provider = new CartErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("INVALID_INPUT");

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }
}
