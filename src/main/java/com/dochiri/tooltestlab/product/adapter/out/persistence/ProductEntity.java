package com.dochiri.tooltestlab.product.adapter.out.persistence;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity {

    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, unique = true, length = 32)
    private String productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private long priceAmount;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false, length = 20)
    private String status;

    @ElementCollection
    @CollectionTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "category_id")
    private List<String> categoryIdValues = new ArrayList<>();

    private ProductEntity(final Long id, final String productId, final String name, final String description,
                         long priceAmount, final int stock, final String status) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.priceAmount = priceAmount;
        this.stock = stock;
        this.status = status;
    }

    static ProductEntity create(final String productId, final String name, final String description,
                                final long priceAmount, final int stock, final String status) {
        return new ProductEntity(null, productId, name, description, priceAmount, stock, status);
    }

    /**
     * Domain 상태를 Entity에 반영한다.
     */
    void apply(final com.dochiri.tooltestlab.product.domain.model.Product domain) {
        this.name = domain.name().value();
        this.description = domain.description().value();
        this.priceAmount = domain.price().amount();
        this.stock = domain.stock().value();
        this.status = domain.status().name();
    }

    /**
     * 연관된 Category ID 목록을 교체한다. PersistenceAdapter에서 managed entity로 동기화할 때만 사용.
     */
    void replaceCategoryIdValues(final List<String> categoryIdValues) {
        this.categoryIdValues.clear();
        this.categoryIdValues.addAll(categoryIdValues);
    }
}
