package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence;

import com.dochiri.tooltestlab.global.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "product_categories",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_categories_public_id",
                        columnNames = "public_id"
                ),
                @UniqueConstraint(
                        name = "uk_product_categories_product_category",
                        columnNames = {"product_id", "category_id"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategoryEntity extends BaseEntity {

    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, updatable = false, length = 36)
    private String publicId;

    @Column(name = "product_id", nullable = false, updatable = false, length = 36)
    private String productId;

    @Column(name = "category_id", nullable = false, updatable = false, length = 36)
    private String categoryId;

    private ProductCategoryEntity(final String publicId, final String productId, final String categoryId) {
        this.publicId = publicId;
        this.productId = productId;
        this.categoryId = categoryId;
    }

    static ProductCategoryEntity create(final String publicId, final String productId, final String categoryId) {
        return new ProductCategoryEntity(publicId, productId, categoryId);
    }

}
