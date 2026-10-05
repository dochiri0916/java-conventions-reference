package com.dochiri.tooltestlab.global.persistence;

import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Set;
import java.util.function.Supplier;

public final class RepositoryExceptionTranslator {

    private RepositoryExceptionTranslator() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다");
    }

    public static <T> T execute(final Supplier<T> operation) {
        try {
            return operation.get();
        } catch (final DataAccessException | PersistenceException exception) {
            throw new RepositoryOperationException(exception);
        }
    }

    public static void run(final Runnable operation) {
        execute(() -> {
            operation.run();
            return null;
        });
    }

    /** 원인 사슬에서 이름이 정확히 일치하는 제약 위반만 찾는다. DB가 붙이는 스키마·인덱스 접미사는 정규화한다. */
    public static boolean isConstraintViolation(final Throwable exception, final String constraintName) {
        final Set<Throwable> visited = Collections.newSetFromMap(new IdentityHashMap<>());
        Throwable cause = exception;
        boolean matches = false;
        while (cause != null && !matches && visited.add(cause)) {
            matches = matchesConstraint(cause, constraintName);
            cause = cause.getCause();
        }
        return matches;
    }

    private static boolean matchesConstraint(final Throwable exception, final String constraintName) {
        if (!(exception instanceof final ConstraintViolationException violation)) {
            return false;
        }
        final String actualName = violation.getConstraintName();
        return actualName != null && normalize(actualName).equals(normalize(constraintName));
    }

    private static String normalize(final String name) {
        final String unquoted = name.replace("\"", "").replace("`", "");
        final String bare = unquoted.substring(unquoted.lastIndexOf('.') + 1);
        return bare.toLowerCase(Locale.ROOT).replaceFirst("_index_[0-9a-f]+$", "");
    }
}
