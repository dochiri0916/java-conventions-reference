package com.dochiri.tooltestlab.global.error;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.Locale;

@RequiredArgsConstructor
public class ApiProblemDetailFactory {

    public static final String CODE_PROPERTY = "code";
    public static final String TIMESTAMP_PROPERTY = "timestamp";
    public static final String TRACE_ID_PROPERTY = "traceId";

    private final URI baseUri;

    public ProblemDetail create(
            final String code,
            final String title,
            final String detail,
            final HttpStatusCode status,
            final ProblemType type
    ) {
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(typeUri(type));
        problemDetail.setProperty(CODE_PROPERTY, code);
        problemDetail.setProperty(TIMESTAMP_PROPERTY, Instant.now());
        return problemDetail;
    }

    private URI typeUri(final ProblemType type) {
        return UriComponentsBuilder.fromUri(baseUri)
                .pathSegment("problems", type.name().toLowerCase(Locale.ROOT))
                .build()
                .toUri();
    }
}
