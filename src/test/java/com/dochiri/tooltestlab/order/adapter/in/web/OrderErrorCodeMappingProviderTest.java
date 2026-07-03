package com.dochiri.tooltestlab.order.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ApiErrorCode;
import com.dochiri.tooltestlab.global.error.ProblemType;
import com.dochiri.tooltestlab.order.application.exception.EmptyCartException;
import com.dochiri.tooltestlab.order.application.exception.NotOrderOwnerException;
import com.dochiri.tooltestlab.order.application.exception.OrderApplicationErrorCode;
import com.dochiri.tooltestlab.order.application.exception.OrderNotFoundException;
import com.dochiri.tooltestlab.order.domain.exception.OrderDomainErrorCode;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderIdException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderLinesException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderStatusException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidQuantityException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException;

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
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(OrderApplicationErrorCode.ORDER_NOT_FOUND));

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
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(OrderApplicationErrorCode.NOT_ORDER_OWNER));

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
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(OrderDomainErrorCode.BAD_ORDER_STATUS));

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
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(OrderApplicationErrorCode.EMPTY_CART));

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
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(OrderDomainErrorCode.ORDER_ID_REQUIRED));

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }

    @Test
    @DisplayName("InvalidOrderIdException을 타입 기반으로 매핑한다")
    void resolveInvalidOrderIdException() {
        // given
        var provider = new OrderErrorCodeMappingProvider();
        var exception = InvalidOrderIdException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(OrderDomainErrorCode.ORDER_ID_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("InvalidQuantityException을 타입 기반으로 매핑한다")
    void resolveInvalidQuantityException() {
        // given
        var provider = new OrderErrorCodeMappingProvider();
        var exception = InvalidQuantityException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(OrderDomainErrorCode.QUANTITY_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("InvalidShippingAddressException을 타입 기반으로 매핑한다")
    void resolveInvalidShippingAddressException() {
        // given
        var provider = new OrderErrorCodeMappingProvider();
        var exception = InvalidShippingAddressException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(OrderDomainErrorCode.SHIPPING_ADDRESS_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("InvalidOrderLinesException을 타입 기반으로 매핑한다")
    void resolveInvalidOrderLinesException() {
        // given
        var provider = new OrderErrorCodeMappingProvider();
        var exception = InvalidOrderLinesException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(OrderDomainErrorCode.ORDER_LINES_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("InvalidOrderStatusException을 타입 기반으로 매핑한다")
    void resolveInvalidOrderStatusException() {
        // given
        var provider = new OrderErrorCodeMappingProvider();
        var exception = InvalidOrderStatusException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(OrderDomainErrorCode.ORDER_STATUS_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("EmptyCartException을 타입 기반으로 매핑한다")
    void resolveEmptyCartException() {
        // given
        var provider = new OrderErrorCodeMappingProvider();
        var exception = EmptyCartException.emptyCart();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(OrderApplicationErrorCode.EMPTY_CART));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("OrderNotFoundException을 타입 기반으로 매핑한다")
    void resolveOrderNotFoundException() {
        // given
        var provider = new OrderErrorCodeMappingProvider();
        var exception = OrderNotFoundException.orderNotFound("order1");

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(OrderApplicationErrorCode.ORDER_NOT_FOUND));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("NotOrderOwnerException을 타입 기반으로 매핑한다")
    void resolveNotOrderOwnerException() {
        // given
        var provider = new OrderErrorCodeMappingProvider();
        var exception = NotOrderOwnerException.notOrderOwner();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(OrderApplicationErrorCode.NOT_ORDER_OWNER));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
