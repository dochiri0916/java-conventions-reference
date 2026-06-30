package com.dochiri.tooltestlab.review.application.service;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.review.application.exception.DuplicateReviewException;
import com.dochiri.tooltestlab.review.application.exception.NotReviewOwnerException;
import com.dochiri.tooltestlab.review.application.exception.ReviewNotFoundException;
import com.dochiri.tooltestlab.review.application.port.in.WriteReviewCommand;
import com.dochiri.tooltestlab.review.application.port.out.ReviewRepositoryPort;
import com.dochiri.tooltestlab.review.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceMockTest {

    @Mock
    private ReviewRepositoryPort reviewRepositoryPort;

    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        reviewService = new ReviewService(reviewRepositoryPort);
    }

    @Test
    @DisplayName("write - 중복 리뷰 예외")
    void writeDuplicate() {
            // given

            // when

            // then

        when(reviewRepositoryPort.existsByMemberIdAndProductId("m1", "p1")).thenReturn(true);
        var cmd = new WriteReviewCommand("m1", "p1", 5, "좋아요");
        assertThatThrownBy(() -> reviewService.write(cmd))
                .isInstanceOf(DuplicateReviewException.class);
    }

    @Test
    @DisplayName("write 성공")
    void writeSuccess() {
            // given

            // when

            // then

        when(reviewRepositoryPort.existsByMemberIdAndProductId("m1", "p1")).thenReturn(false);
        when(reviewRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        var cmd = new WriteReviewCommand("m1", "p1", 5, "좋아요");
        var result = reviewService.write(cmd);
        assertThat(result.publicId()).isNotNull();
    }

    @Test
    @DisplayName("getByProduct - 빈 리스트")
    void getByProductEmpty() {
            // given

            // when

            // then

        when(reviewRepositoryPort.findByProductId("p1")).thenReturn(List.of());
        var result = reviewService.getByProduct("p1");
        assertThat(result.reviews()).isEmpty();
        assertThat(result.averageRating()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("delete - 리뷰 없음 예외")
    void deleteNotFound() {
            // given

            // when

            // then

        when(reviewRepositoryPort.findByPublicId("nonexistent")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> reviewService.delete("nonexistent", "m1"))
                .isInstanceOf(ReviewNotFoundException.class);
    }

    @Test
    @DisplayName("delete - 리뷰 소유자 아님 예외")
    void deleteNotOwner() {
            // given

            // when

            // then

        var review = Review.create(new MemberId("owner"), new ProductId("p1"), new Rating(5), new ReviewContent("좋아요"));
        when(reviewRepositoryPort.findByPublicId(review.id().value())).thenReturn(Optional.of(review));
        assertThatThrownBy(() -> reviewService.delete(review.id().value(), "otherUser"))
                .isInstanceOf(NotReviewOwnerException.class);
    }
}
