package com.dochiri.tooltestlab.review.adapter.out.persistence;

import com.dochiri.tooltestlab.global.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews", uniqueConstraints = {
        @UniqueConstraint(name = "uk_reviews_member_product", columnNames = {"memberId", "productId"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewEntity extends BaseEntity {

    @Getter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter(AccessLevel.NONE)
    @Version
    private Long version;

    @Column(nullable = false, unique = true, length = 36)
    private String reviewId;

    @Column(nullable = false, length = 36)
    private String memberId;

    @Column(nullable = false, length = 36)
    private String productId;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false, length = 1000)
    private String content;

    private ReviewEntity(final String reviewId, final String memberId,
                        final String productId, final int rating, final String content) {
        this.reviewId = reviewId;
        this.memberId = memberId;
        this.productId = productId;
        this.rating = rating;
        this.content = content;
    }

    static ReviewEntity create(final String reviewId, final String memberId, final String productId,
                               final int rating, final String content) {
        return new ReviewEntity(reviewId, memberId, productId, rating, content);
    }
}
