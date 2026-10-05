package com.dochiri.tooltestlab.global.domain.outbox;

import com.dochiri.tooltestlab.global.domain.exception.InvalidOutboxAttemptsException;
import com.dochiri.tooltestlab.global.domain.exception.InvalidOutboxSequenceException;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

public abstract class Outbox {
    private final Message message;
    private final Delivery delivery;

    protected Outbox(final Message message) {
        this(message, new Delivery(null, message.occurredAt(), 0, null));
    }

    protected Outbox(final Message message, final Delivery delivery) {
        this.message = requireNonNull(message);
        this.delivery = requireNonNull(delivery);
    }

    public abstract String source();

    protected abstract Outbox withDelivery(Delivery delivery);

    public Message message() {
        return message;
    }

    public Delivery delivery() {
        return delivery;
    }

    public boolean ready(final Instant now) {
        return delivery.publishedAt() == null && !
                delivery.nextAttemptAt()
                        .isAfter(now);
    }

    public final Outbox published(final Instant now) {
        return withDelivery(new Delivery(requireNonNull(now), delivery.nextAttemptAt(),
                delivery.attempts(), null));
    }

    public final Outbox failed(final Instant now, final Throwable failure) {
        final int attempts = delivery.attempts() + 1;
        final Instant nextAttemptAt = now.plusSeconds(
                Math.min(60, 1L <<
                        Math.min(attempts - 1, 6)));
        final String error = failure.getClass()
                .getSimpleName() + ": " +
                        failure.getMessage();
        return withDelivery(new Delivery(null, nextAttemptAt, attempts, error.substring(0,
                Math.min(
                        error.length(), 1000))));
    }

    public record Message(String eventId, String eventType, String aggregateId, long sequence,
                          Instant occurredAt, String payload) {
        public Message {
            requireNonNull(eventId);
            requireNonNull(eventType);
            requireNonNull(aggregateId);
            requireNonNull(occurredAt);
            requireNonNull(payload);
            if (sequence < 0) {
                throw new InvalidOutboxSequenceException();
            }
        }
    }

    public record Delivery(Instant publishedAt, Instant nextAttemptAt, int attempts, String lastError) {
        public Delivery {
            requireNonNull(nextAttemptAt);
            if (attempts < 0) {
                throw new InvalidOutboxAttemptsException();
            }
        }
    }
}
