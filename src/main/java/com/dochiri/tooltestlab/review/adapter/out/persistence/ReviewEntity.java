package com.dochiri.tooltestlab.review.adapter.out.persistence;

import jakarta.persistence.*;
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

    public ReviewEntity(Long id, String publicId, String memberPublicId, String productPublicId, int rating, String content) {
        this.id = id;
        this.publicId = publicId;
        this.memberPublicId = memberPublicId;
        this.productPublicId = productPublicId;
        this.rating = rating;
        this.content = content;
    }
}
