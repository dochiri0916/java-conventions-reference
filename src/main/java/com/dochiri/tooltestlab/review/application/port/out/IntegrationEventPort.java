package com.dochiri.tooltestlab.review.application.port.out;

import com.dochiri.tooltestlab.review.application.port.out.event.ReviewIntegrationEvent;

public interface IntegrationEventPort {
    void publish(ReviewIntegrationEvent event);
}
