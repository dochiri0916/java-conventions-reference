package com.dochiri.tooltestlab.global.problem;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
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
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {

    private final ProblemDetailFactory problemDetailFactory;

    @ExceptionHandler(ErrorCodeException.class)
    public ResponseEntity<ProblemDetail> handleErrorCodeException(ErrorCodeException exception) {
        ProblemDetail body = problemDetailFactory.create(exception);
        return ResponseEntity.status(exception.errorCode().status()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpectedException(Exception exception) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred."
        );

        body.setTitle("Internal Server Error");
        body.setProperty("code", "INTERNAL_SERVER_ERROR");

        return ResponseEntity.internalServerError().body(body);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(
                status,
                "Request validation failed."
        );

        body.setTitle("Validation failed");
        body.setProperty("code", "VALIDATION_ERROR");

        List<Map<String, String>> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", Objects.requireNonNullElse(error.getDefaultMessage(), "Invalid value")
                ))
                .toList();

        body.setProperty("errors", errors);

        return handleExceptionInternal(exception, body, headers, status, request);
    }

}
