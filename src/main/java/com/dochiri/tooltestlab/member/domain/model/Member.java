package com.dochiri.tooltestlab.member.domain.model;

import static java.util.Objects.requireNonNull;

/**
 * 회원 Aggregate Root.
 */
public record Member(
        MemberId id,
        String email,
        String password,
        String name,
        MemberStatus status) {

    public Member {
        requireNonNull(id, "id는 null일 수 없습니다.");
        requireNonNull(email, "이메일은 null일 수 없습니다.");
        requireNonNull(password, "비밀번호는 null일 수 없습니다.");
        requireNonNull(name, "이름은 null일 수 없습니다.");
        requireNonNull(status, "회원 상태는 null일 수 없습니다.");

        email = email.strip();
        name = name.strip();

        if (email.isBlank()) throw new IllegalArgumentException("이메일은 빈 값일 수 없습니다.");
        if (password.isBlank()) throw new IllegalArgumentException("비밀번호는 빈 값일 수 없습니다.");
        if (name.isBlank()) throw new IllegalArgumentException("이름은 빈 값일 수 없습니다.");
    }

    /**
     * 신규 회원 생성.
     */
    public static Member create(String email, String encodedPassword, String name) {
        return new Member(
                MemberId.generate(),
                email,
                encodedPassword,
                name,
                MemberStatus.ACTIVE);
    }

    /**
     * 회원 탈퇴 처리.
     */
    public Member withdraw() {
        return new Member(id, email, password, name, MemberStatus.WITHDRAWN);
    }
}
