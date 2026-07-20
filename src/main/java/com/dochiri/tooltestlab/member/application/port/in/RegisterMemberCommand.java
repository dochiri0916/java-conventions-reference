package com.dochiri.tooltestlab.member.application.port.in;

import com.dochiri.tooltestlab.member.application.exception.MemberApplicationException;

public record RegisterMemberCommand(String email, String password, String name) {
    public RegisterMemberCommand {
        if (email == null || email.isBlank()) {
            throw MemberApplicationException.emailRequired();
        }
        if (password == null || password.isBlank()) {
            throw MemberApplicationException.passwordRequired();
        }
        if (name == null || name.isBlank()) {
            throw MemberApplicationException.nameRequired();
        }
    }
}
