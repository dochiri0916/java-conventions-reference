package com.dochiri.tooltestlab.member.application.port.in;

import com.dochiri.tooltestlab.member.application.exception.InvalidCommandException;

public record LoginMemberCommand(String email, String password) {
    public LoginMemberCommand {
        if (email == null || email.isBlank()) {
            throw new InvalidCommandException("이메일은 빈 값일 수 없습니다.");
        }
        if (password == null || password.isBlank()) {
            throw new InvalidCommandException("비밀번호는 빈 값일 수 없습니다.");
        }
    }
}
