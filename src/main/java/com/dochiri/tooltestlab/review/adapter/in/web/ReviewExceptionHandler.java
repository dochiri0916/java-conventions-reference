package com.dochiri.tooltestlab.review.adapter.in.web;

import com.dochiri.tooltestlab.review.domain.exception.InvalidProductReferenceIdException;
import com.dochiri.tooltestlab.review.domain.exception.InvalidMemberReferenceIdException;
import com.dochiri.tooltestlab.global.exception.ProblemDetails;
import com.dochiri.tooltestlab.review.application.exception.DuplicateReviewException;
import com.dochiri.tooltestlab.review.application.exception.InvalidCommandException;
import com.dochiri.tooltestlab.review.domain.exception.NotReviewOwnerException;
import com.dochiri.tooltestlab.review.application.exception.ReviewApplicationException;
import com.dochiri.tooltestlab.review.application.exception.ReviewNotFoundException;
import com.dochiri.tooltestlab.review.domain.exception.InvalidRatingException;
import com.dochiri.tooltestlab.review.domain.exception.InvalidReviewContentException;
import com.dochiri.tooltestlab.review.domain.exception.InvalidReviewIdException;
import com.dochiri.tooltestlab.review.domain.exception.MemberReferenceIdRequiredException;
import com.dochiri.tooltestlab.review.domain.exception.ProductReferenceIdRequiredException;
import com.dochiri.tooltestlab.review.domain.exception.RatingRequiredException;
import com.dochiri.tooltestlab.review.domain.exception.ReviewContentRequiredException;
import com.dochiri.tooltestlab.review.domain.exception.ReviewDomainException;
import com.dochiri.tooltestlab.review.domain.exception.ReviewIdRequiredException;
import com.dochiri.tooltestlab.review.domain.exception.ReviewRequiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class ReviewExceptionHandler {

    private final ProblemDetails problemDetails;

    @ExceptionHandler(ReviewDomainException.class)
    public ProblemDetail handle(final ReviewDomainException exception) {
        return switch (exception) {
            case InvalidProductReferenceIdException e -> respond(e, HttpStatus.BAD_REQUEST, "PRODUCT_ID_INVALID");
            case InvalidMemberReferenceIdException e -> respond(e, HttpStatus.BAD_REQUEST, "MEMBER_ID_INVALID");
            case ReviewIdRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "REVIEW_ID_REQUIRED");
            case InvalidReviewIdException e -> respond(e, HttpStatus.BAD_REQUEST, "REVIEW_ID_INVALID");
            case InvalidRatingException e -> respond(e, HttpStatus.BAD_REQUEST, "RATING_INVALID");
            case RatingRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "RATING_REQUIRED");
            case InvalidReviewContentException e -> respond(e, HttpStatus.BAD_REQUEST, "REVIEW_CONTENT_INVALID");
            case ReviewContentRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "REVIEW_CONTENT_REQUIRED");
            case ReviewRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "REVIEW_REQUIRED");
            case ProductReferenceIdRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "PRODUCT_ID_REQUIRED");
            case MemberReferenceIdRequiredException e -> respond(e, HttpStatus.BAD_REQUEST, "MEMBER_ID_REQUIRED");
            case NotReviewOwnerException e -> respond(e, HttpStatus.FORBIDDEN, "NOT_REVIEW_OWNER");
        };
    }

    @ExceptionHandler(ReviewApplicationException.class)
    public ProblemDetail handle(final ReviewApplicationException exception) {
        return switch (exception) {
            case DuplicateReviewException e -> respond(e, HttpStatus.CONFLICT, "DUPLICATE_REVIEW");
            case ReviewNotFoundException e -> respond(e, HttpStatus.NOT_FOUND, "REVIEW_NOT_FOUND");
            case InvalidCommandException e -> respond(e, HttpStatus.BAD_REQUEST, "INVALID_INPUT");
        };
    }

    private ProblemDetail respond(final RuntimeException exception, final HttpStatus status, final String code) {
        log.info("Review 예외가 발생했습니다. type={}, code={}, message={}",
                exception.getClass().getSimpleName(), code, exception.getMessage());
        return problemDetails.create(status, code, exception.getMessage());
    }

}
