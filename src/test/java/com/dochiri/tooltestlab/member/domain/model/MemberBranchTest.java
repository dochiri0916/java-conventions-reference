package com.dochiri.tooltestlab.member.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberBranchTest {

    @Test
    @DisplayName("회원 탈퇴")
    void withdraw() {
            // given

            // when

            // then

        var member = Member.create(new Email("test@test.com"), new Password("pw"), new MemberName("홍길동"));
        var withdrawn = member.withdraw();
        assertThat(withdrawn.status()).isEqualTo(MemberStatus.WITHDRAWN);
    }

    @Test
    @DisplayName("equals - 다른 타입 false")
    void equalsDifferentType() {
            // given

            // when

            // then

        var member = Member.create(new Email("test@test.com"), new Password("pw"), new MemberName("홍길동"));
        assertThat(member.equals("string")).isFalse();
    }

    @Test
    @DisplayName("equals - 다른 인스턴스 같은 ID")
    void equalsDifferentInstanceSameId() {
            // given

            // when

            // then

        var m1 = Member.create(new Email("a@a.com"), new Password("pw"), new MemberName("A"));
        var m2 = new Member(m1.id(), new Email("b@b.com"), new Password("pw2"), new MemberName("B"), MemberStatus.ACTIVE);
        assertThat(m1.equals(m2)).isTrue();
        assertThat(m1.hashCode()).isEqualTo(m2.hashCode());
    }

    @Test
    @DisplayName("equals - 자기 자신과 비교하면 true")
    void equalsSelf() {
            // given
        var member = Member.create(new Email("test@test.com"), new Password("pw"), new MemberName("홍길동"));

            // when
        var result = member.equals(member);

            // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("equals - 다른 ID를 가지면 false")
    void equalsDifferentId() {
            // given
        var m1 = Member.create(new Email("a@a.com"), new Password("pw"), new MemberName("A"));
        var m2 = Member.create(new Email("b@b.com"), new Password("pw2"), new MemberName("B"));

            // when
        var result = m1.equals(m2);

            // then
        assertThat(result).isFalse();
    }
}
