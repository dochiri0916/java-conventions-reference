package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, unique = true, length = 32)
    private String publicId;

    @Column(nullable = false, unique = true, length = 32)
    private String memberPublicId;

    public CartEntity(final Long id, final String publicId, final String memberPublicId) {
        this.id = id;
        this.publicId = publicId;
        this.memberPublicId = memberPublicId;
    }
}
