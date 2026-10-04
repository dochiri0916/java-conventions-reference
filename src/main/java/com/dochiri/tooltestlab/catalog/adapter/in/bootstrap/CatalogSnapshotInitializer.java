package com.dochiri.tooltestlab.catalog.adapter.in.bootstrap;

import com.dochiri.tooltestlab.catalog.application.port.in.PublishProductSnapshotsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "messaging.snapshots.publish-on-startup", havingValue = "true", matchIfMissing = true)
public final class CatalogSnapshotInitializer {
    private final PublishProductSnapshotsUseCase publishSnapshotsUseCase;

    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        for (int page = 0; publishSnapshotsUseCase.publishPage(page, 100) == 100; page++) {
            // 각 페이지는 독립 트랜잭션으로 현재 상태를 Outbox에 기록한다.
        }
    }
}
