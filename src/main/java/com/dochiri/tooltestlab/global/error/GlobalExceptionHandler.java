package com.dochiri.tooltestlab.global.error;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR_CODE = ApiErrorCode.from(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    private static final String VALIDATION_ERROR_CODE = ApiErrorCode.from(GlobalErrorCode.VALIDATION_ERROR);
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
                .orElseGet(this::handleUnexpected);
    }

    private ResponseEntity<ProblemDetail> handleUnexpected() {
        final ApiErrorMessage message = messageCatalog.internalError();
        final ProblemDetail body = problemDetailFactory.create(
                INTERNAL_SERVER_ERROR_CODE,
                message.title(),
                message.detail(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ProblemType.INTERNAL_ERROR
        );
        return ResponseEntity.internalServerError().body(body);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        final ApiErrorMessage message = messageCatalog.validationError();
        final String defaultFieldMessage = messageCatalog.defaultFieldErrorMessage();
        final ProblemDetail body = problemDetailFactory.create(
                VALIDATION_ERROR_CODE,
                message.title(),
                message.detail(),
                status,
                ProblemType.VALIDATION_FAILED
        );

        List<Map<String, String>> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of(
                        FIELD_PROPERTY, error.getField(),
                        MESSAGE_PROPERTY, Objects.requireNonNullElse(error.getDefaultMessage(), defaultFieldMessage)))
                .toList();
        body.setProperty(ERRORS_PROPERTY, errors);

        return handleExceptionInternal(exception, body, headers, status, request);
    }

    private ResponseEntity<ProblemDetail> mapToResponse(final ApiExceptionMapper.MappedError mappedError) {
        final ApiExceptionMapper.Mapping mapping = mappedError.mapping();
        final ApiErrorMessage message = messageCatalog.findByCode(mappedError.code());
        final ProblemDetail body = problemDetailFactory.create(
                mappedError.code(),
                message.title(),
                message.detail(),
                mapping.status(),
                mapping.problemType()
        );
        return ResponseEntity.status(mapping.status()).body(body);
    }
}
