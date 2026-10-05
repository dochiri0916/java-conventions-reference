package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence;

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
        name = "products",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_products_public_id",
                columnNames = "public_id"
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity extends BaseEntity {

    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter(AccessLevel.NONE)
    @Version
    private Long version;

    @Column(name = "public_id", nullable = false, updatable = false, length = 36)
    private String publicId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name = "price_amount", nullable = false)
    private long priceAmount;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "status", nullable = false)
    private String status;

    private ProductEntity(
            final String publicId,
            final String name,
            final String description,
            final long priceAmount,
            final int stock,
            final String status
    ) {
        this.publicId = publicId;
        this.name = name;
        this.description = description;
        this.priceAmount = priceAmount;
        this.stock = stock;
        this.status = status;
    }

    static ProductEntity create(
            final String publicId,
            final String name,
            final String description,
            final long priceAmount,
            final int stock,
            final String status
    ) {
        return new ProductEntity(
                publicId,
                name,
                description,
                priceAmount,
                stock,
                status
        );
    }

    void update(
            final String name,
            final String description,
            final long priceAmount,
            final int stock,
            final String status
    ) {
        this.name = name;
        this.description = description;
        this.priceAmount = priceAmount;
        this.stock = stock;
        this.status = status;
    }

}
