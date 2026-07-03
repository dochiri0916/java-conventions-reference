package com.dochiri.tooltestlab.order.application.port.out;

import com.dochiri.tooltestlab.order.domain.event.OrderCancelled;
import com.dochiri.tooltestlab.order.domain.event.OrderPlaced;

public interface OrderEventPort {

    void publishAfterCommit(OrderPlaced event);

    void publishAfterCommit(OrderCancelled event);
}
