package com.dochiri.tooltestlab.global.adapter.in.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.time.Instant;

public final class ProblemDetails {

    private ProblemDetails() {

    }

    public static ProblemDetail create(HttpStatus status, String code, String detail) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);

        problemDetail.setTitle(status.getReasonPhrase());
        problemDetail.setProperty("code", code);
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

}
