package com.dochiri.tooltestlab.member.adapter.out.persistence.outbox;

import com.dochiri.tooltestlab.global.messaging.persistence.OutboxEntry;
import com.dochiri.tooltestlab.member.domain.outbox.MemberOutbox;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_outbox", indexes = @Index(name = "idx_member_outbox_pending", columnList = "publishedAt,id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberOutboxEntry extends OutboxEntry {
    public MemberOutboxEntry(final MemberOutbox outbox) {
        super(outbox);
    }

    @Override
    public MemberOutbox toDomain() {
        return new MemberOutbox(message(), delivery());
    }
}
