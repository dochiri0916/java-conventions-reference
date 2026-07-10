package com.dochiri.tooltestlab.order.adapter.in.event;

import com.dochiri.tooltestlab.order.application.port.in.HandleOrderCancelledUseCase;
import com.dochiri.tooltestlab.order.domain.event.OrderCancelled;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class OrderCancelledEventListener {

    private final List<HandleOrderCancelledUseCase> handleOrderCancelledUseCases;

    @EventListener
    public void on(final OrderCancelled event) {
        handleOrderCancelledUseCases.forEach(useCase -> useCase.handleOrderCancelled(event));
    }
}
