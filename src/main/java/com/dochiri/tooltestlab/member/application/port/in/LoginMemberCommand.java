package com.dochiri.tooltestlab.member.application.port.in;

import com.dochiri.tooltestlab.member.application.exception.MemberApplicationException;

public record LoginMemberCommand(String email, String password) {
    public LoginMemberCommand {
        if (email == null || email.isBlank()) {
            throw MemberApplicationException.emailRequired();
        }
        if (password == null || password.isBlank()) {
            throw MemberApplicationException.passwordRequired();
        }
    }
}
