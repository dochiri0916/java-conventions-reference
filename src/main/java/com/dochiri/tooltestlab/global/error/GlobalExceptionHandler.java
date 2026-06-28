package com.dochiri.tooltestlab.global.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ProblemDetailFactory problemDetailFactory;

    public GlobalExceptionHandler(ProblemDetailFactory problemDetailFactory) {
        this.problemDetailFactory = problemDetailFactory;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ex.errorCode();

        ProblemDetail problemDetail = problemDetailFactory.create(
                errorCode.httpStatus(),
                errorCode.code(),
                errorCode.message(),
                ex.getMessage(),
                request
        );

        return ResponseEntity
                .status(errorCode.httpStatus())
                .body(problemDetail);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = CommonErrorCode.VALIDATION_FAILED;

        ProblemDetail problemDetail = problemDetailFactory.create(
                errorCode.httpStatus(),
                errorCode.code(),
                errorCode.message(),
                ex.getMessage(),
                request
        );

        return ResponseEntity
                .status(errorCode.httpStatus())
                .body(problemDetail);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ProblemDetail> handleMissingRequestHeader(
            MissingRequestHeaderException ex,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = CommonErrorCode.MISSING_REQUEST_HEADER;

        ProblemDetail problemDetail = problemDetailFactory.create(
                errorCode.httpStatus(),
                errorCode.code(),
                errorCode.message(),
                errorCode.message(),
                request
        );

        return ResponseEntity
                .status(errorCode.httpStatus())
                .body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest webRequest
    ) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();

        ErrorCode errorCode = CommonErrorCode.VALIDATION_FAILED;

        ProblemDetail problemDetail = problemDetailFactory.create(
                errorCode.httpStatus(),
                errorCode.code(),
                errorCode.message(),
                errorCode.message(),
                request
        );

        List<FieldErrorResponse> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldErrorResponse::from)
                .toList();

        problemDetail.setProperty("errors", errors);

        return ResponseEntity
                .status(errorCode.httpStatus())
                .headers(headers)
                .body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest webRequest
    ) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();

        ErrorCode errorCode = CommonErrorCode.HTTP_MESSAGE_NOT_READABLE;

        ProblemDetail problemDetail = problemDetailFactory.create(
                errorCode.httpStatus(),
                errorCode.code(),
                errorCode.message(),
                errorCode.message(),
                request
        );

        return ResponseEntity
                .status(errorCode.httpStatus())
                .headers(headers)
                .body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            org.springframework.beans.TypeMismatchException ex,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest webRequest
    ) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();

        ErrorCode errorCode = CommonErrorCode.TYPE_MISMATCH;

        ProblemDetail problemDetail = problemDetailFactory.create(
                errorCode.httpStatus(),
                errorCode.code(),
                errorCode.message(),
                errorCode.message(),
                request
        );

        return ResponseEntity
                .status(errorCode.httpStatus())
                .headers(headers)
                .body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest webRequest
    ) {
        HttpServletRequest request;
        if (webRequest instanceof ServletWebRequest servletWebRequest) {
            request = servletWebRequest.getRequest();
        } else {
            // 웹 요청이 아닌 경우 간단히 처리
            ProblemDetail problemDetail = problemDetailFactory.createSimple(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    CommonErrorCode.INTERNAL_SERVER_ERROR.code(),
                    CommonErrorCode.INTERNAL_SERVER_ERROR.message(),
                    CommonErrorCode.INTERNAL_SERVER_ERROR.message()
            );

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(headers)
                    .body(problemDetail);
        }

        // Spring이 전달한 실제 HTTP 상태 코드를 사용한다.
        // handleExceptionInternal로 전달되는 예외들은 각자 올바른 상태 코드를 가지고 있으므로
        // INTERNAL_SERVER_ERROR로 하드코딩하지 않는다.
        HttpStatus resolvedStatus = HttpStatus.valueOf(statusCode.value());

        ProblemDetail problemDetail = problemDetailFactory.create(
                resolvedStatus,
                CommonErrorCode.INTERNAL_SERVER_ERROR.code(),
                resolvedStatus.getReasonPhrase(),
                ex.getMessage() != null ? ex.getMessage() : resolvedStatus.getReasonPhrase(),
                request
        );

        return ResponseEntity
                .status(resolvedStatus)
                .headers(headers)
                .body(problemDetail);
    }

}
