package com.dochiri.tooltestlab.member.application.port.in;

import com.dochiri.tooltestlab.member.application.exception.InvalidCommandException;

public record LoginMemberCommand(String email, String password) {
    public LoginMemberCommand {
        if (email == null || email.isBlank()) {
            throw InvalidCommandException.emailRequired();
        }
        if (password == null || password.isBlank()) {
            throw InvalidCommandException.passwordRequired();
        }
    }
}
