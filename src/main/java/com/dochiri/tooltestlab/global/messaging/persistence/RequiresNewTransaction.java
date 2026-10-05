package com.dochiri.tooltestlab.global.messaging.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

/** 발행·수신·정리를 호출자 트랜잭션과 분리된 새 트랜잭션에서 실행한다. */
@Component
@RequiredArgsConstructor
public final class RequiresNewTransaction {
    private final PlatformTransactionManager transactionManager;

    public <T> T execute(final Supplier<T> action) {
        final var template = new TransactionTemplate(transactionManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return template.execute(status -> action.get());
    }

    public void run(final Runnable action) {
        execute(() -> {
            action.run();
            return null;
        });
    }
}
