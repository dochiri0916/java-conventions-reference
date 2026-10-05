package com.dochiri.tooltestlab.global.messaging.persistence;

/** BC별 Inbox 영속 구현을 소비자 이름으로 찾도록 어댑터가 등록하는 계약. */
public interface InboxRegistration {
    String consumer();

    Class<? extends InboxEntry> type();

    InboxProcessor.EntryFactory factory();
}
