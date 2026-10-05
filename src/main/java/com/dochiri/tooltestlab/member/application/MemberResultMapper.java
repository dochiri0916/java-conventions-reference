package com.dochiri.tooltestlab.member.application;

import com.dochiri.tooltestlab.member.application.port.in.result.LoginMemberResult;
import com.dochiri.tooltestlab.member.application.port.in.result.RegisterMemberResult;
import com.dochiri.tooltestlab.member.domain.Member;
import com.dochiri.tooltestlab.member.domain.vo.Email;
import com.dochiri.tooltestlab.member.domain.vo.MemberId;
import com.dochiri.tooltestlab.member.domain.vo.MemberName;

public final class MemberResultMapper {

    private MemberResultMapper() {
    }

    public static RegisterMemberResult toRegisterResult(final Member member) {
        final MemberId id = member.id();
        final Email email = member.email();
        final MemberName name = member.name();
        return new RegisterMemberResult(id.value(),
                email.value(),
                name.value());
    }

    public static LoginMemberResult toLoginResult(final Member member) {
        final MemberId id = member.id();
        final Email email = member.email();
        final MemberName name = member.name();
        return new LoginMemberResult(id.value(),
                email.value(),
                name.value());
    }
}
