package com.dochiri.tooltestlab.order.adapter.in.event;

import com.dochiri.tooltestlab.order.application.port.in.HandleOrderPlacedUseCase;
import com.dochiri.tooltestlab.order.domain.event.OrderPlaced;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class OrderPlacedEventListener {

    private final List<HandleOrderPlacedUseCase> handleOrderPlacedUseCases;

    @EventListener
    public void on(final OrderPlaced event) {
        handleOrderPlacedUseCases.forEach(useCase -> useCase.handleOrderPlaced(event));
    }
}
