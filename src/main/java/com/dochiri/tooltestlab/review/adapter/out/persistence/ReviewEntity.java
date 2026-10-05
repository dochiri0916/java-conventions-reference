package com.dochiri.tooltestlab.review.adapter.out.persistence;

import com.dochiri.tooltestlab.global.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "reviews",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_reviews_public_id",
                        columnNames = "public_id"
                ),
                @UniqueConstraint(
                        name = "uk_reviews_member_product",
                        columnNames = {"member_id", "product_id"}
                )
        }
)
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

    @Column(name = "public_id", nullable = false, updatable = false, length = 36)
    private String publicId;

    @Column(name = "member_id", nullable = false, length = 36)
    private String memberId;

    @Column(name = "product_id", nullable = false, length = 36)
    private String productId;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false, length = 1000)
    private String content;

    private ReviewEntity(final String publicId, final String memberId,
                        final String productId, final int rating, final String content) {
        this.publicId = publicId;
        this.memberId = memberId;
        this.productId = productId;
        this.rating = rating;
        this.content = content;
    }

    static ReviewEntity create(final String publicId, final String memberId, final String productId,
                               final int rating, final String content) {
        return new ReviewEntity(publicId, memberId, productId, rating, content);
    }
}
