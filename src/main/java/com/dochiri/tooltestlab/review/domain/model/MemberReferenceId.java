package com.dochiri.tooltestlab.review.domain.model;

import com.dochiri.tooltestlab.review.domain.exception.MemberReferenceIdRequiredException;

public record MemberReferenceId(String value) {

    public MemberReferenceId {
        if (value == null || value.isBlank()) {
            throw new MemberReferenceIdRequiredException();
        }
        value = value.strip();
    }

}
