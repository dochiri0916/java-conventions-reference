package com.dochiri.tooltestlab.review.application.service;

import com.dochiri.tooltestlab.review.domain.exception.NotReviewOwnerException;
import com.dochiri.tooltestlab.review.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.review.application.port.out.ReviewRepositoryPort;
import com.dochiri.tooltestlab.review.application.port.out.event.ReviewDeletedIntegrationEvent;
import com.dochiri.tooltestlab.review.domain.Review;
import com.dochiri.tooltestlab.review.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.Rating;
import com.dochiri.tooltestlab.review.domain.vo.ReviewContent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteReviewServiceTest {

    @Test
    @DisplayName("작성자 UUID의 대소문자가 달라도 같은 회원의 리뷰를 삭제한다")
    void deletesReviewForTheSameOwnerWithUppercaseUuid() {
        // given
        final Review review = review();
        final ReviewRepositoryPort reviews = mock(ReviewRepositoryPort.class);
        final IntegrationEventPort events = mock(IntegrationEventPort.class);
        final DeleteReviewService service = new DeleteReviewService(reviews, events);
        when(reviews.findByReviewId(review.id())).thenReturn(Optional.of(review));

        // when
        assertDoesNotThrow(() -> service.delete(review.id().value().toUpperCase(Locale.ROOT),
                review.memberId().value().toUpperCase(Locale.ROOT)));

        // then
        verify(reviews).deleteByReviewId(review.id());
        verify(events).publish(new ReviewDeletedIntegrationEvent(review.id().value(), review.productId().value()));
    }

    @Test
    @DisplayName("다른 회원은 리뷰를 삭제하거나 삭제 이벤트를 발행할 수 없다")
    void rejectsAnotherOwner() {
        // given
        final Review review = review();
        final ReviewRepositoryPort reviews = mock(ReviewRepositoryPort.class);
        final IntegrationEventPort events = mock(IntegrationEventPort.class);
        final DeleteReviewService service = new DeleteReviewService(reviews, events);
        when(reviews.findByReviewId(review.id())).thenReturn(Optional.of(review));

        // when
        // then
        assertThrows(NotReviewOwnerException.class, () -> service.delete(review.id().value(),
                "01900000-0000-7000-8000-00000000000c"));
        verify(reviews, never()).deleteByReviewId(any());
        verifyNoInteractions(events);
    }

    private static Review review() {
        return Review.create(
                MemberReferenceId.parse("01900000-0000-7000-8000-00000000000a"),
                ProductReferenceId.parse("01900000-0000-7000-8000-00000000000b"),
                Rating.of(5), ReviewContent.of("리뷰 내용입니다"));
    }
}
