package com.dochiri.tooltestlab.member.application.command;

public record LoginMemberCommand(String email, String password) {
    public LoginMemberCommand {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 빈 값일 수 없습니다.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 빈 값일 수 없습니다.");
        }
    }
}
