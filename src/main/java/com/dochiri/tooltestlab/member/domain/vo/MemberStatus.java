package com.dochiri.tooltestlab.member.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus {
    ACTIVE("활성"),
    WITHDRAWN("탈퇴");

    private final String description;
}
