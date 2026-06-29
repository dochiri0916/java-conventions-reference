package com.dochiri.tooltestlab.review.adapter.out.persistence;

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
@Table(name = "reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"memberPublicId", "productPublicId"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, unique = true, length = 32)
    private String publicId;

    @Column(nullable = false, length = 32)
    private String memberPublicId;

    @Column(nullable = false, length = 32)
    private String productPublicId;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false, length = 1000)
    private String content;

    public ReviewEntity(final Long id, final String publicId, final String memberPublicId,
                        String productPublicId, final int rating, final String content) {
        this.id = id;
        this.publicId = publicId;
        this.memberPublicId = memberPublicId;
        this.productPublicId = productPublicId;
        this.rating = rating;
        this.content = content;
    }
}
