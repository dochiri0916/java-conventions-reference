package com.dochiri.tooltestlab.category.adapter.out.persistence;

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
@Table(name = "categories", uniqueConstraints = {
        @UniqueConstraint(name = "uk_categories_category_id", columnNames = "categoryId"),
        @UniqueConstraint(name = "uk_categories_name", columnNames = "name")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryEntity {

    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, unique = true, length = 32)
    private String categoryId;

    @Column(nullable = false)
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
