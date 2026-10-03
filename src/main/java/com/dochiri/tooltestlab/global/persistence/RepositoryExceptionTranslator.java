package com.dochiri.tooltestlab.global.persistence;

import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;
import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataAccessException;

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
}
