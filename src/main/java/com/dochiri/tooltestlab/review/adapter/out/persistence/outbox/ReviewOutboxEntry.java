package com.dochiri.tooltestlab.review.adapter.out.persistence.outbox;

import com.dochiri.tooltestlab.global.messaging.persistence.OutboxEntry;
import com.dochiri.tooltestlab.review.domain.outbox.ReviewOutbox;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_outbox", indexes = @Index(name = "idx_review_outbox_pending", columnList = "publishedAt,id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewOutboxEntry extends OutboxEntry {
    public ReviewOutboxEntry(final ReviewOutbox outbox) {
        super(outbox);
    }

    @Override
    public ReviewOutbox toDomain() {
        return new ReviewOutbox(message(), delivery());
    }
}
