package com.dochiri.tooltestlab.global.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.Locale;

@Component
public class ApiProblemDetailFactory {

    private final URI baseUri;

    public ApiProblemDetailFactory(@Value("${api.problem.base-uri}") URI baseUri) {
        this.baseUri = baseUri;
    }

    public ProblemDetail create(final ApiErrorCode errorCode, final String detail) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(errorCode.status(), detail);
        problemDetail.setTitle(errorCode.title());
        problemDetail.setType(typeUri(errorCode.type()));
        problemDetail.setProperty("code", errorCode.code());
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    public ProblemDetail create(final String code, final String title, final String detail, final HttpStatus status, final ProblemType type) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(typeUri(type));
        problemDetail.setProperty("code", code);
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    private URI typeUri(final String type) {
        return UriComponentsBuilder.fromUri(baseUri)
                .pathSegment("problems", type)
                .build()
                .toUri();
    }

    private URI typeUri(final ProblemType type) {
        return UriComponentsBuilder.fromUri(baseUri)
                .pathSegment("problems", type.name().toLowerCase(Locale.ROOT))
                .build()
                .toUri();
    }
}
