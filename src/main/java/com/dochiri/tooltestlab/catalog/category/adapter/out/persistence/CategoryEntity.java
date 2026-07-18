package com.dochiri.tooltestlab.catalog.category.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_categories_public_id",
                        columnNames = "public_id"
                ),
                @UniqueConstraint(
                        name = "uk_categories_name",
                        columnNames = "name"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryEntity {

    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter(AccessLevel.NONE)
    @Version
    private Long version;

    @Column(name = "public_id", nullable = false, unique = true, length = 32)
    private String publicId;

    @Column(name = "name", nullable = false)
    private String name;

    private CategoryEntity(
            final String publicId,
            final String name
    ) {
        this.publicId = publicId;
        this.name = name;
    }

    static CategoryEntity create(
            final String publicId,
            final String name
    ) {
        return new CategoryEntity(
                publicId,
                name
        );
    }

    void updateName(final String name) {
        this.name = name;
    }
}
