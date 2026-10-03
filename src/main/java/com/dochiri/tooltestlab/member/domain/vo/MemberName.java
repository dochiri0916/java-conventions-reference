package com.dochiri.tooltestlab.member.domain.vo;

import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberNameException;
import com.dochiri.tooltestlab.member.domain.exception.MemberNameRequiredException;

public record MemberName(String value) {

    public MemberName {
        if (value == null) {
            throw new MemberNameRequiredException();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw new InvalidMemberNameException();
        }
    }
}
