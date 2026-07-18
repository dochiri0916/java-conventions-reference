package com.dochiri.tooltestlab.review.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiErrorCode;
import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ErrorCodeMappingProvider;
import com.dochiri.tooltestlab.global.error.ProblemType;
import com.dochiri.tooltestlab.review.application.exception.*;
import com.dochiri.tooltestlab.review.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class ReviewErrorCodeMappingProvider implements ErrorCodeMappingProvider {

    @Override
    public Map<String, ApiExceptionMapper.Mapping> errorCodeMappings() {
        return Map.ofEntries(
                validation(ReviewDomainErrorCode.INVALID_REVIEW_ID),
                validation(ReviewDomainErrorCode.REVIEW_ID_REQUIRED),
                validation(ReviewDomainErrorCode.INVALID_RATING),
                validation(ReviewDomainErrorCode.RATING_REQUIRED),
                validation(ReviewDomainErrorCode.BAD_RVW_CONTENT),
                validation(ReviewDomainErrorCode.REVIEW_CONTENT_REQUIRED),
                validation(ReviewDomainErrorCode.INVALID_REVIEW),
                validation(ReviewDomainErrorCode.REVIEW_REQUIRED),
                notFound(ReviewApplicationErrorCode.REVIEW_NOT_FOUND),
                conflict(ReviewApplicationErrorCode.DUPLICATE_REVIEW),
                forbidden(ReviewApplicationErrorCode.NOT_REVIEW_OWNER),
                validation(ReviewApplicationErrorCode.INVALID_INPUT)
        );
    }

    @Override
    public Optional<ApiExceptionMapper.MappedError> resolve(final RuntimeException exception) {
        if (exception instanceof InvalidReviewIdException invalidReviewIdException) {
            return resolve(invalidReviewIdException.code());
        }
        if (exception instanceof InvalidRatingException invalidRatingException) {
            return resolve(invalidRatingException.code());
        }
        if (exception instanceof InvalidReviewContentException invalidReviewContentException) {
            return resolve(invalidReviewContentException.code());
        }
        if (exception instanceof InvalidReviewException invalidReviewException) {
            return resolve(invalidReviewException.code());
        }
        if (exception instanceof ReviewNotFoundException reviewNotFoundException) {
            return resolve(reviewNotFoundException.code());
        }
        if (exception instanceof DuplicateReviewException duplicateReviewException) {
            return resolve(duplicateReviewException.code());
        }
        if (exception instanceof NotReviewOwnerException notReviewOwnerException) {
            return resolve(notReviewOwnerException.code());
        }
        if (exception instanceof InvalidCommandException invalidCommandException) {
            return resolve(invalidCommandException.code());
        }
        return Optional.empty();
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> validation(final Enum<?> errorCode) {
        return mapping(errorCode, HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED);
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> notFound(final Enum<?> errorCode) {
        return mapping(errorCode, HttpStatus.NOT_FOUND, ProblemType.NOT_FOUND);
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> conflict(final Enum<?> errorCode) {
        return mapping(errorCode, HttpStatus.CONFLICT, ProblemType.CONFLICT);
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> forbidden(final Enum<?> errorCode) {
        return mapping(errorCode, HttpStatus.FORBIDDEN, ProblemType.FORBIDDEN);
    }

    private static Map.Entry<String, ApiExceptionMapper.Mapping> mapping(
            final Enum<?> errorCode,
            final HttpStatus status,
            final ProblemType problemType
    ) {
        return Map.entry(ApiErrorCode.from(errorCode), new ApiExceptionMapper.Mapping(status, problemType));
    }
}
