package com.dochiri.tooltestlab.product.adapter.out.persistence;

import jakarta.persistence.*;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, unique = true, length = 32)
    private String publicId;

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
    private List<Long> categoryIds = new ArrayList<>();

    public ProductEntity(Long id, String publicId, String name, String description, long priceAmount, int stock, String status) {
        this.id = id;
        this.publicId = publicId;
        this.name = name;
        this.description = description;
        this.priceAmount = priceAmount;
        this.stock = stock;
        this.status = status;
    }

    /**
     * Domain 상태를 Entity에 반영한다.
     */
    void apply(com.dochiri.tooltestlab.product.domain.model.Product domain) {
        this.name = domain.name().value();
        this.description = domain.description().value();
        this.priceAmount = domain.price().amount();
        this.stock = domain.stock().value();
        this.status = domain.status().name();
    }

    /**
     * 연관된 Category ID 목록을 교체한다. PersistenceAdapter에서 managed entity로 동기화할 때만 사용.
     */
    void replaceCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
