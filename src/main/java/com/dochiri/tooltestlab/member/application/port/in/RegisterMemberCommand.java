package com.dochiri.tooltestlab.member.application.port.in;

import com.dochiri.tooltestlab.member.application.exception.InvalidCommandException;

public record RegisterMemberCommand(String email, String password, String name) {
    public RegisterMemberCommand {
        if (email == null || email.isBlank()) {
            throw InvalidCommandException.emailRequired();
        }
        if (password == null || password.isBlank()) {
            throw InvalidCommandException.passwordRequired();
        }
        if (name == null || name.isBlank()) {
            throw InvalidCommandException.nameRequired();
        }
    }
}
