package com.dochiri.tooltestlab.cart.application.service;

import com.dochiri.tooltestlab.cart.application.CartEventMapper;
import com.dochiri.tooltestlab.cart.application.port.in.PublishCartSnapshotsUseCase;
import com.dochiri.tooltestlab.cart.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public final class PublishCartSnapshotsService implements PublishCartSnapshotsUseCase {
    private final CartRepositoryPort cartRepositoryPort;
    private final IntegrationEventPort integrationEventPort;

    @Override
    @Transactional
    public int publishPage(final int page, final int size) {
        final var snapshots = cartRepositoryPort.findPage(page, size);
        snapshots.stream().sorted(Comparator.comparing(snapshot -> snapshot.id().value())).forEach(snapshot ->
                cartRepositoryPort.findByMemberIdForUpdate(snapshot.memberId().value()).ifPresent(current ->
                        integrationEventPort.publish(CartEventMapper.toIntegration(current.changedEvent()))));
        return snapshots.size();
    }
}
