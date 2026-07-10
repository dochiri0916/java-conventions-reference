package com.dochiri.tooltestlab.global.error;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public final class ApiErrorContractValidator {

    private final List<ErrorCodeMappingProvider> mappingProviders;
    private final List<ApiErrorMessageProvider> messageProviders;

    @PostConstruct
    void validate() {
        final Map<String, Integer> mappingOccurrences = occurrences(mappingProviders.stream()
                .map(ErrorCodeMappingProvider::errorCodeMappings)
                .toList());
        final Map<String, Integer> messageOccurrences = occurrences(messageProviders.stream()
                .map(ApiErrorMessageProvider::errorMessages)
                .toList());

        validateUnique("API 오류 매핑", mappingOccurrences);
        validateUnique("API 오류 메시지", messageOccurrences);

        final Set<String> mappingCodes = mappingOccurrences.keySet();
        final Set<String> messageCodes = messageOccurrences.keySet();
        if (!mappingCodes.equals(messageCodes)) {
            throw new IllegalStateException("API 오류 매핑과 메시지 카탈로그가 일치하지 않습니다: mapping="
                    + mappingCodes + ", messages=" + messageCodes);
        }
    }

    private static Map<String, Integer> occurrences(final List<? extends Map<String, ?>> entries) {
        final Map<String, Integer> occurrences = new HashMap<>();
        for (final Map<String, ?> entry : entries) {
            for (final String code : entry.keySet()) {
                occurrences.merge(code, 1, Integer::sum);
            }
        }
        return Map.copyOf(occurrences);
    }

    private static void validateUnique(final String contractName, final Map<String, Integer> occurrences) {
        final Set<String> duplicateCodes = occurrences.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
        if (!duplicateCodes.isEmpty()) {
            throw new IllegalStateException(contractName + "에 중복 코드가 있습니다: " + duplicateCodes);
        }
    }
}
