package com.dochiri.tooltestlab.catalog.category.adapter.out.persistence;

import com.dochiri.tooltestlab.global.persistence.BaseEntity;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryStatus;
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
public class CategoryEntity extends BaseEntity {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private CategoryStatus status;

    private CategoryEntity(
            final String publicId,
            final String name,
            final CategoryStatus status
    ) {
        this.publicId = publicId;
        this.name = name;
        this.status = status;
    }

    static CategoryEntity create(
            final String publicId,
            final String name,
            final CategoryStatus status
    ) {
        return new CategoryEntity(publicId, name, status);
    }

    void update(final String name, final CategoryStatus status) {
        this.name = name;
        this.status = status;
    }
}
