package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import com.dochiri.tooltestlab.global.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carts")
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

    @Column(nullable = false, unique = true, length = 36)
    private String cartId;

    @Column(nullable = false, unique = true, length = 36)
    private String memberId;

    private CartEntity(final String cartId, final String memberId) {
        this.cartId = cartId;
        this.memberId = memberId;
    }

    static CartEntity create(final String cartId, final String memberId) {
        return new CartEntity(cartId, memberId);
    }
}
