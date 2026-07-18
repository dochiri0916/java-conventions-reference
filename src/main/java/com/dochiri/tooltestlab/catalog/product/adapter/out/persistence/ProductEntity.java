package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.product.domain.model.ProductStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_products_public_id",
                        columnNames = "public_id"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity {

    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter(AccessLevel.NONE)
    @Version
    private Long version;

    @Column(name = "public_id", nullable = false, updatable = false, length = 32)
    private String publicId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name = "price_amount", nullable = false)
    private long priceAmount;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatus status;

    @Getter(AccessLevel.NONE)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "product_category_references",
            joinColumns = @JoinColumn(
                    name = "product_id",
                    nullable = false
            ),
            uniqueConstraints = {
                    @UniqueConstraint(
                            name = "uk_product_category_references",
                            columnNames = {
                                    "product_id",
                                    "category_reference_id"
                            }
                    )
            }
    )
    @Column(
            name = "category_reference_id",
            nullable = false,
            length = 32
    )
    private Set<String> categoryReferenceIds = new HashSet<>();

    private ProductEntity(
            final String publicId,
            final String name,
            final String description,
            long priceAmount,
            final int stock,
            final ProductStatus status
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
            final ProductStatus status
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
            final ProductStatus status
    ) {
        this.name = name;
        this.description = description;
        this.priceAmount = priceAmount;
        this.stock = stock;
        this.status = status;
    }

    Set<String> categoryReferenceIds() {
        return Set.copyOf(categoryReferenceIds);
    }

    void replaceCategoryReferenceIds(final Set<String> categoryReferenceIds) {
        final Set<String> replacement = Set.copyOf(categoryReferenceIds);

        this.categoryReferenceIds.clear();
        this.categoryReferenceIds.addAll(replacement);
    }

}
