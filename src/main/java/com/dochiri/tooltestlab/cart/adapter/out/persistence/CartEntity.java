package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartEntity {

    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, unique = true, length = 32)
    private String cartId;

    @Column(nullable = false, unique = true, length = 32)
    private String memberId;

    private CartEntity(final Long id, final String cartId, final String memberId) {
        this.id = id;
        this.cartId = cartId;
        this.memberId = memberId;
    }

    static CartEntity create(final String cartId, final String memberId) {
        return new CartEntity(null, cartId, memberId);
    }
}
