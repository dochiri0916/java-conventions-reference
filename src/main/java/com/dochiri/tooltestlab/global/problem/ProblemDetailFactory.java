package com.dochiri.tooltestlab.global.problem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;

@Component
public class ProblemDetailFactory {

    private final URI baseUri;

    public ProblemDetailFactory(@Value("${api.problem.base-uri}") URI baseUri) {
        this.baseUri = baseUri;
    }

    public ProblemDetail create(ErrorCodeException exception) {
        ErrorCode errorCode = exception.errorCode();

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                errorCode.status(),
                exception.detail()
        );

        problemDetail.setTitle(errorCode.title());
        problemDetail.setType(typeUri(errorCode));
        problemDetail.setProperty("code", errorCode.code());
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    public ProblemDetail create(ErrorCode errorCode, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                errorCode.status(),
                detail
        );

        problemDetail.setTitle(errorCode.title());
        problemDetail.setType(typeUri(errorCode));
        problemDetail.setProperty("code", errorCode.code());
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    private URI typeUri(ErrorCode errorCode) {
        return UriComponentsBuilder.fromUri(baseUri)
                .pathSegment("problems", errorCode.type())
                .build()
                .toUri();
    }

}
