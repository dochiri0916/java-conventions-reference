package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import com.dochiri.tooltestlab.global.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "carts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_carts_public_id",
                        columnNames = "public_id"
                ),
                @UniqueConstraint(
                        name = "uk_carts_member_id",
                        columnNames = "member_id"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartEntity extends BaseEntity {

    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter(AccessLevel.NONE)
    @Version
    private Long version;

    @Column(name = "public_id", nullable = false, length = 36)
    private String publicId;

    @Column(name = "member_id", nullable = false, length = 36)
    private String memberId;

    @Column(nullable = false)
    private long revision;

    void updateRevision(final long revision) {
        this.revision = revision;
    }

    private CartEntity(final String publicId, final String memberId) {
        this.publicId = publicId;
        this.memberId = memberId;
    }

    static CartEntity create(final String publicId, final String memberId) {
        return new CartEntity(publicId, memberId);
    }
}
