package com.dochiri.tooltestlab.review.application.port.out.event;

import com.dochiri.tooltestlab.global.messaging.IntegrationEvent;

public sealed interface ReviewIntegrationEvent extends IntegrationEvent
        permits ReviewWrittenIntegrationEvent, ReviewDeletedIntegrationEvent {
}
