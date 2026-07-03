package com.dochiri.tooltestlab.global.error;

import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public final class ApiErrorCode {

    private static final String SEPARATOR = ".";
    private static final Set<String> LAYER_SEGMENTS = Set.of("domain", "application", "adapter");

    private ApiErrorCode() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다");
    }

    public static String from(final Enum<?> errorCode) {
        final Enum<?> code = Objects.requireNonNull(errorCode, "errorCode");
        return namespace(code.getDeclaringClass()) + SEPARATOR + code.name();
    }

    private static String namespace(final Class<?> errorCodeType) {
        final String packageName = errorCodeType.getPackageName();
        if (packageName.endsWith(".global.error") || packageName.contains(".global.error.")) {
            return "GLOBAL";
        }

        final String[] segments = packageName.split("\\.");
        for (int index = 1; index < segments.length; index++) {
            if (LAYER_SEGMENTS.contains(segments[index])) {
                return segments[index - 1].toUpperCase(Locale.ROOT);
            }
        }

        return fallbackNamespace(errorCodeType);
    }

    private static String fallbackNamespace(final Class<?> errorCodeType) {
        final String simpleName = errorCodeType.getSimpleName()
                .replace("DomainErrorCode", "")
                .replace("ApplicationErrorCode", "")
                .replace("ErrorCode", "");
        if (simpleName.isBlank()) {
            return "GLOBAL";
        }
        return simpleName.toUpperCase(Locale.ROOT);
    }
}
