package com.dochiri.tooltestlab.member.application.port.in.command;

import com.dochiri.tooltestlab.member.application.exception.InvalidCommandException;

public record RegisterMemberCommand(String email, String password, String name) {
    public RegisterMemberCommand {
        if (email == null || email.isBlank()) {
            throw new InvalidCommandException("이메일은 빈 값일 수 없습니다.");
        }
        if (password == null || password.isBlank()) {
            throw new InvalidCommandException("비밀번호는 빈 값일 수 없습니다.");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidCommandException("회원 이름은 빈 값일 수 없습니다.");
        }
    }
}
