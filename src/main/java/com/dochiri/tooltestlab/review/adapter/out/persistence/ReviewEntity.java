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
        @UniqueConstraint(name = "uk_reviews_member_product", columnNames = {"memberId", "productId"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewEntity {

    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, unique = true, length = 32)
    private String reviewId;

    @Column(nullable = false, length = 32)
    private String memberId;

    @Column(nullable = false, length = 32)
    private String productId;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false, length = 1000)
    private String content;

    private ReviewEntity(final Long id, final String reviewId, final String memberId,
                        final String productId, final int rating, final String content) {
        this.id = id;
        this.reviewId = reviewId;
        this.memberId = memberId;
        this.productId = productId;
        this.rating = rating;
        this.content = content;
    }

    static ReviewEntity create(final String reviewId, final String memberId, final String productId,
                               final int rating, final String content) {
        return new ReviewEntity(null, reviewId, memberId, productId, rating, content);
    }
}
