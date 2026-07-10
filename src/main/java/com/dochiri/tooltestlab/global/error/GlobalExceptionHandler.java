package com.dochiri.tooltestlab.global.error;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String TRACE_ID_MDC_KEY = "traceId";
    private static final String ERRORS_PROPERTY = "errors";
    private static final String FIELD_PROPERTY = "field";
    private static final String MESSAGE_PROPERTY = "message";

    private final ApiProblemDetailFactory problemDetailFactory;
    private final ApiExceptionMapper exceptionMapper;
    private final ApiErrorMessageCatalog messageCatalog;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetail> handleRuntime(final RuntimeException exception) {
        return exceptionMapper.map(exception)
                .map(this::mapToResponse)
                .orElseGet(() -> handleUnexpected(exception));
    }

    private ResponseEntity<ProblemDetail> handleUnexpected(final RuntimeException exception) {
        LOG.error("매핑되지 않은 RuntimeException을 처리합니다: {}", exception.getClass().getName(), exception);
        final String errorCode = ApiErrorCode.from(GlobalErrorCode.INTERNAL_SERVER_ERROR);
        final ApiErrorMessage message = messageCatalog.findByCode(errorCode);
        final ProblemDetail body = createProblemDetail(
                errorCode,
                message,
                HttpStatus.INTERNAL_SERVER_ERROR,
                ProblemType.INTERNAL_ERROR
        );
        return ResponseEntity.internalServerError().body(body);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException exception,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        final String errorCode = ApiErrorCode.from(GlobalErrorCode.VALIDATION_ERROR);
        final ProblemDetail body = createProblemDetail(errorCode, status);
        final String defaultFieldMessage = messageCatalog.defaultFieldErrorMessage();
        final List<Map<String, String>> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of(
                        FIELD_PROPERTY, error.getField(),
                        MESSAGE_PROPERTY, Objects.requireNonNullElse(error.getDefaultMessage(), defaultFieldMessage)))
                .toList();
        body.setProperty(ERRORS_PROPERTY, errors);
        return handleExceptionInternal(exception, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            final HttpMessageNotReadableException exception,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        return handleMvcException(exception, GlobalErrorCode.MALFORMED_REQUEST_BODY, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            final TypeMismatchException exception,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        return handleMvcException(exception, GlobalErrorCode.PARAMETER_TYPE_MISMATCH, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException exception,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        return handleMvcException(exception, GlobalErrorCode.MISSING_PARAMETER, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            final MissingPathVariableException exception,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        return handleMvcException(exception, GlobalErrorCode.MISSING_PARAMETER, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
            final ServletRequestBindingException exception,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        return handleMvcException(exception, GlobalErrorCode.MISSING_PARAMETER, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException exception,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        return handleMvcException(exception, GlobalErrorCode.METHOD_NOT_ALLOWED, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            final NoHandlerFoundException exception,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        return handleMvcException(exception, GlobalErrorCode.ROUTE_NOT_FOUND, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
            final NoResourceFoundException exception,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        return handleMvcException(exception, GlobalErrorCode.ROUTE_NOT_FOUND, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            final HttpMediaTypeNotSupportedException exception,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        return handleMvcException(exception, GlobalErrorCode.UNSUPPORTED_MEDIA_TYPE, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            final HttpMediaTypeNotAcceptableException exception,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        return handleMvcException(exception, GlobalErrorCode.NOT_ACCEPTABLE, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            final HandlerMethodValidationException exception,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        return handleMvcException(exception, GlobalErrorCode.VALIDATION_ERROR, headers, status, request);
    }

    private ResponseEntity<Object> handleMvcException(
            final Exception exception,
            final GlobalErrorCode errorCode,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        final ProblemDetail body = createProblemDetail(ApiErrorCode.from(errorCode), status);
        return handleExceptionInternal(exception, body, headers, status, request);
    }

    private ProblemDetail createProblemDetail(final String errorCode, final HttpStatusCode status) {
        final ApiExceptionMapper.Mapping mapping = exceptionMapper.map(errorCode);
        final ApiErrorMessage message = messageCatalog.findByCode(errorCode);
        return createProblemDetail(errorCode, message, status, mapping.problemType());
    }

    private ProblemDetail createProblemDetail(
            final String errorCode,
            final ApiErrorMessage message,
            final HttpStatusCode status,
            final ProblemType problemType) {
        final ProblemDetail body = problemDetailFactory.create(
                errorCode,
                message.title(),
                message.detail(),
                status,
                problemType
        );
        body.setProperty(ApiProblemDetailFactory.TRACE_ID_PROPERTY, traceId());
        return body;
    }

    private ResponseEntity<ProblemDetail> mapToResponse(final ApiExceptionMapper.MappedError mappedError) {
        final ApiExceptionMapper.Mapping mapping = mappedError.mapping();
        final ApiErrorMessage message = messageCatalog.findByCode(mappedError.code());
        final ProblemDetail body = createProblemDetail(
                mappedError.code(),
                message,
                mapping.status(),
                mapping.problemType()
        );
        return ResponseEntity.status(mapping.status()).body(body);
    }

    private String traceId() {
        final String traceId = MDC.get(TRACE_ID_MDC_KEY);
        if (traceId != null && !traceId.isBlank()) {
            return traceId;
        }
        return UUID.randomUUID().toString().replace("-", "");
    }
}
