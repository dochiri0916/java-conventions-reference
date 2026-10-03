package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.cart.domain.exception.MemberReferenceIdRequiredException;

public record MemberReferenceId(String value) {

    public MemberReferenceId {
        if (value == null || value.isBlank()) {
            throw new MemberReferenceIdRequiredException();
        }
        value = value.strip();
    }

}
