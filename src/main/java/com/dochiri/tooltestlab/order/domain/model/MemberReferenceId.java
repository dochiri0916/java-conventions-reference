package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.MemberReferenceIdRequiredException;

public record MemberReferenceId(String value) {

    public MemberReferenceId {
        if (value == null || value.isBlank()) {
            throw new MemberReferenceIdRequiredException();
        }
        value = value.strip();
    }

}
