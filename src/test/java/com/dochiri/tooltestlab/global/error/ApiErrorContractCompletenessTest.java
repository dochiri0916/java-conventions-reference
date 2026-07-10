package com.dochiri.tooltestlab.global.error;

import com.dochiri.tooltestlab.cart.adapter.in.web.CartErrorCodeMappingProvider;
import com.dochiri.tooltestlab.cart.adapter.in.web.CartErrorMessageProvider;
import com.dochiri.tooltestlab.category.adapter.in.web.CategoryErrorCodeMappingProvider;
import com.dochiri.tooltestlab.category.adapter.in.web.CategoryErrorMessageProvider;
import com.dochiri.tooltestlab.member.adapter.in.web.MemberErrorCodeMappingProvider;
import com.dochiri.tooltestlab.member.adapter.in.web.MemberErrorMessageProvider;
import com.dochiri.tooltestlab.order.adapter.in.web.OrderErrorCodeMappingProvider;
import com.dochiri.tooltestlab.order.adapter.in.web.OrderErrorMessageProvider;
import com.dochiri.tooltestlab.product.adapter.in.web.ProductErrorCodeMappingProvider;
import com.dochiri.tooltestlab.product.adapter.in.web.ProductErrorMessageProvider;
import com.dochiri.tooltestlab.review.adapter.in.web.ReviewErrorCodeMappingProvider;
import com.dochiri.tooltestlab.review.adapter.in.web.ReviewErrorMessageProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ApiErrorContractCompletenessTest {

    @Test
    @DisplayName("모든 API 오류 매핑은 정확히 하나의 메시지를 가진다")
    void everyMappingHasExactlyOneMessage() {
        // given
        List<ErrorCodeMappingProvider> mappingProviders = List.of(
                new GlobalErrorCodeMappingProvider(),
                new CartErrorCodeMappingProvider(),
                new CategoryErrorCodeMappingProvider(),
                new MemberErrorCodeMappingProvider(),
                new OrderErrorCodeMappingProvider(),
                new ProductErrorCodeMappingProvider(),
                new ReviewErrorCodeMappingProvider());
        List<ApiErrorMessageProvider> messageProviders = List.of(
                new GlobalErrorMessageProvider(),
                new CartErrorMessageProvider(),
                new CategoryErrorMessageProvider(),
                new MemberErrorMessageProvider(),
                new OrderErrorMessageProvider(),
                new ProductErrorMessageProvider(),
                new ReviewErrorMessageProvider());

        // when
        Map<String, ApiExceptionMapper.Mapping> mappings = mergeMappings(mappingProviders);
        Map<String, ApiErrorMessage> messages = mergeMessages(messageProviders);

        // then
        assertThat(mappings).containsOnlyKeys(messages.keySet());
        assertThat(messages).containsOnlyKeys(mappings.keySet());
    }

    private static Map<String, ApiExceptionMapper.Mapping> mergeMappings(
            final List<ErrorCodeMappingProvider> providers) {
        final Map<String, ApiExceptionMapper.Mapping> result = new HashMap<>();
        for (final ErrorCodeMappingProvider provider : providers) {
            for (final Map.Entry<String, ApiExceptionMapper.Mapping> entry
                    : provider.errorCodeMappings().entrySet()) {
                assertThat(result.putIfAbsent(entry.getKey(), entry.getValue())).isNull();
            }
        }
        return result;
    }

    private static Map<String, ApiErrorMessage> mergeMessages(final List<ApiErrorMessageProvider> providers) {
        final Map<String, ApiErrorMessage> result = new HashMap<>();
        for (final ApiErrorMessageProvider provider : providers) {
            for (final Map.Entry<String, ApiErrorMessage> entry : provider.errorMessages().entrySet()) {
                assertThat(result.putIfAbsent(entry.getKey(), entry.getValue())).isNull();
            }
        }
        return result;
    }
}
