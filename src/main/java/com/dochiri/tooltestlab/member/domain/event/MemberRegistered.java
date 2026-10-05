package com.dochiri.tooltestlab.member.domain.event;

import com.dochiri.tooltestlab.member.domain.exception.MemberIdRequiredException;
import com.dochiri.tooltestlab.member.domain.vo.MemberId;

public record MemberRegistered(MemberId memberId) {

    public MemberRegistered {
        if (memberId == null) {
            throw new MemberIdRequiredException();
        }
    }

}
