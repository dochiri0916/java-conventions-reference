package com.dochiri.tooltestlab.category.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuppressWarnings("PMD.ImmutableField")
public class CategoryEntity {

    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, unique = true, length = 32)
    private String categoryId;

    @Column(nullable = false, unique = true)
    private String name;

    private CategoryEntity(final Long id, final String categoryId, final String name) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
    }

    public static CategoryEntity create(final String categoryId, final String name) {
        return new CategoryEntity(null, categoryId, name);
    }
}
