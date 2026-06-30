package com.dochiri.tooltestlab.global.error;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ApiProblemDetailFactory problemDetailFactory;
    private final ApiExceptionMapper exceptionMapper;
    private final MessageSource messageSource;

    public GlobalExceptionHandler(final ApiProblemDetailFactory problemDetailFactory,
                                   ApiExceptionMapper exceptionMapper,
                                   MessageSource messageSource) {
        this.problemDetailFactory = problemDetailFactory;
        this.exceptionMapper = exceptionMapper;
        this.messageSource = messageSource;
    }

    // ── Domain / Application 예외 (code() 메서드로 error code 추출) ─────

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetail> handleRuntime(RuntimeException ex) {
        try {
            final Method codeMethod = ex.getClass().getMethod("code");
            final Object errorCode = codeMethod.invoke(ex);
            if (errorCode instanceof Enum<?> ec) {
                String codeName = ec.name();
                String title = codeName;
                try {
                    final Method titleMethod = ec.getClass().getMethod("title");
                    title = (String) titleMethod.invoke(ec);
                } catch (NoSuchMethodException ignored) {
                }
                return mapToResponse(codeName, title, ex.getMessage());
            }
        } catch (ReflectiveOperationException ignored) { /* fallthrough */ }
        return handleUnexpected(ex);
    }

    // ── Fallback ───────────────────────────────────────────────────────

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private ResponseEntity<ProblemDetail> handleUnexpected(Exception ex) {
        final Locale locale = LocaleContextHolder.getLocale();
        final String title = messageSource.getMessage("error.internal.title", null, "Internal Server Error", locale);
        final String detail = messageSource.getMessage("error.internal.detail", null, "An unexpected error occurred.", locale);
        final ProblemDetail body = problemDetailFactory.create(
                "INTERNAL_SERVER_ERROR", title,
                detail, HttpStatus.INTERNAL_SERVER_ERROR, ProblemType.INTERNAL_ERROR);
        return ResponseEntity.internalServerError().body(body);
    }

    // ── Bean Validation ────────────────────────────────────────────────

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        final Locale locale = LocaleContextHolder.getLocale();
        final String detail = messageSource.getMessage("error.validation.detail", null, "Request validation failed.", locale);
        final String title = messageSource.getMessage("error.validation.title", null, "Validation failed", locale);
        final ProblemDetail body = ProblemDetail.forStatusAndDetail(status, detail);
        body.setTitle(title);
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
