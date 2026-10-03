package com.dochiri.tooltestlab.member.domain.vo;

public enum MemberStatus {
    ACTIVE("활성"),
    WITHDRAWN("탈퇴");

    private final String description;

    MemberStatus(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
