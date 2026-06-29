package com.dochiri.tooltestlab.global.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ApiProblemDetailFactory problemDetailFactory;
    private final ApiExceptionMapper exceptionMapper;

    public GlobalExceptionHandler(final ApiProblemDetailFactory problemDetailFactory,
                                   ApiExceptionMapper exceptionMapper) {
        this.problemDetailFactory = problemDetailFactory;
        this.exceptionMapper = exceptionMapper;
    }

    // ── Order Domain ───────────────────────────────────────────────────

    @ExceptionHandler(com.dochiri.tooltestlab.order.domain.exception.OrderDomainException.class)
    public ResponseEntity<ProblemDetail> handleOrderDomain(
            com.dochiri.tooltestlab.order.domain.exception.OrderDomainException ex) {
        return mapToResponse(ex.errorCode().name(), ex.errorCode().title(), ex.getMessage());
    }

    // ── Product Domain ─────────────────────────────────────────────────

    @ExceptionHandler(com.dochiri.tooltestlab.product.domain.exception.ProductDomainException.class)
    public ResponseEntity<ProblemDetail> handleProductDomain(
            com.dochiri.tooltestlab.product.domain.exception.ProductDomainException ex) {
        return mapToResponse(ex.errorCode().name(), ex.errorCode().title(), ex.getMessage());
    }

    // ── Cart Domain ────────────────────────────────────────────────────

    @ExceptionHandler(com.dochiri.tooltestlab.cart.domain.exception.CartDomainException.class)
    public ResponseEntity<ProblemDetail> handleCartDomain(
            com.dochiri.tooltestlab.cart.domain.exception.CartDomainException ex) {
        return mapToResponse(ex.errorCode().name(), ex.errorCode().title(), ex.getMessage());
    }

    // ── Member Domain ──────────────────────────────────────────────────

    @ExceptionHandler(com.dochiri.tooltestlab.member.domain.exception.MemberDomainException.class)
    public ResponseEntity<ProblemDetail> handleMemberDomain(
            com.dochiri.tooltestlab.member.domain.exception.MemberDomainException ex) {
        return mapToResponse(ex.errorCode().name(), ex.errorCode().title(), ex.getMessage());
    }

    // ── Category Domain ────────────────────────────────────────────────

    @ExceptionHandler(com.dochiri.tooltestlab.category.domain.exception.CategoryDomainException.class)
    public ResponseEntity<ProblemDetail> handleCategoryDomain(
            com.dochiri.tooltestlab.category.domain.exception.CategoryDomainException ex) {
        return mapToResponse(ex.errorCode().name(), ex.errorCode().title(), ex.getMessage());
    }

    // ── Review Domain ──────────────────────────────────────────────────

    @ExceptionHandler(com.dochiri.tooltestlab.review.domain.exception.ReviewDomainException.class)
    public ResponseEntity<ProblemDetail> handleReviewDomain(
            com.dochiri.tooltestlab.review.domain.exception.ReviewDomainException ex) {
        return mapToResponse(ex.errorCode().name(), ex.errorCode().title(), ex.getMessage());
    }

    // ── Order Application ──────────────────────────────────────────────

    @ExceptionHandler(com.dochiri.tooltestlab.order.application.exception.OrderApplicationException.class)
    public ResponseEntity<ProblemDetail> handleOrderApplication(
            com.dochiri.tooltestlab.order.application.exception.OrderApplicationException ex) {
        return mapToResponse(ex.errorCode().name(), ex.errorCode().title(), ex.getMessage());
    }

    // ── Cart Application ───────────────────────────────────────────────

    @ExceptionHandler(com.dochiri.tooltestlab.cart.application.exception.CartApplicationException.class)
    public ResponseEntity<ProblemDetail> handleCartApplication(
            com.dochiri.tooltestlab.cart.application.exception.CartApplicationException ex) {
        return mapToResponse(ex.errorCode().name(), ex.errorCode().title(), ex.getMessage());
    }

    // ── Member Application ─────────────────────────────────────────────

    @ExceptionHandler(com.dochiri.tooltestlab.member.application.exception.MemberApplicationException.class)
    public ResponseEntity<ProblemDetail> handleMemberApplication(
            com.dochiri.tooltestlab.member.application.exception.MemberApplicationException ex) {
        return mapToResponse(ex.errorCode().name(), ex.errorCode().title(), ex.getMessage());
    }

    // ── Product Application ────────────────────────────────────────────

    @ExceptionHandler(com.dochiri.tooltestlab.product.application.exception.ProductApplicationException.class)
    public ResponseEntity<ProblemDetail> handleProductApplication(
            com.dochiri.tooltestlab.product.application.exception.ProductApplicationException ex) {
        return mapToResponse(ex.errorCode().name(), ex.errorCode().title(), ex.getMessage());
    }

    // ── Review Application ─────────────────────────────────────────────

    @ExceptionHandler(com.dochiri.tooltestlab.review.application.exception.ReviewApplicationException.class)
    public ResponseEntity<ProblemDetail> handleReviewApplication(
            com.dochiri.tooltestlab.review.application.exception.ReviewApplicationException ex) {
        return mapToResponse(ex.errorCode().name(), ex.errorCode().title(), ex.getMessage());
    }

    // ── Fallback ───────────────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpected(Exception ex) {
        final ProblemDetail body = problemDetailFactory.create(
                "INTERNAL_SERVER_ERROR", "Internal Server Error",
                "An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR, ProblemType.INTERNAL_ERROR);
        return ResponseEntity.internalServerError().body(body);
    }

    // ── Bean Validation ────────────────────────────────────────────────

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        final ProblemDetail body = ProblemDetail.forStatusAndDetail(status, "Request validation failed.");
        body.setTitle("Validation failed");
        body.setProperty("code", "VALIDATION_ERROR");

        List<Map<String, String>> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", Objects.requireNonNullElse(error.getDefaultMessage(), "Invalid value")))
                .toList();
        body.setProperty("errors", errors);

        return handleExceptionInternal(exception, body, headers, status, request);
    }

    private ResponseEntity<ProblemDetail> mapToResponse(final String errorCode, final String title, final String detail) {
        final ApiExceptionMapper.Mapping mapping = exceptionMapper.map(errorCode);
        final ProblemDetail body = problemDetailFactory.create(
                errorCode, title, detail, mapping.status(), mapping.problemType());
        return ResponseEntity.status(mapping.status()).body(body);
    }
}
