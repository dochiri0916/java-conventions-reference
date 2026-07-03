package com.dochiri.tooltestlab.member.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {

    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String memberId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String encodedPassword;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 20)
    private String status;

    private MemberEntity(
            final Long id,
            final String memberId,
            final String email,
            final String encodedPassword,
            final String name,
            final String status) {
        this.id = id;
        this.memberId = memberId;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.name = name;
        this.status = status;
    }

    public static MemberEntity create(
            final String memberId,
            final String email,
            final String encodedPassword,
            final String name,
            final String status) {
        return new MemberEntity(null, memberId, email, encodedPassword, name, status);
    }
}
