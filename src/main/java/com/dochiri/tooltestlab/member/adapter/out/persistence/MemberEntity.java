package com.dochiri.tooltestlab.member.adapter.out.persistence;

import com.dochiri.tooltestlab.global.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "members",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_members_email",
                columnNames = "email"
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class MemberEntity extends BaseEntity {

    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, length = 36)
    private String publicId;

    @Column(nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String encodedPassword;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 20)
    private String status;

    private MemberEntity(
            final Long id,
            final String publicId,
            final String email,
            final String encodedPassword,
            final String name,
            final String status) {
        this.id = id;
        this.publicId = publicId;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.name = name;
        this.status = status;
    }

    public static MemberEntity create(
            final String publicId,
            final String email,
            final String encodedPassword,
            final String name,
            final String status) {
        return new MemberEntity(null, publicId, email, encodedPassword, name, status);
    }
}
