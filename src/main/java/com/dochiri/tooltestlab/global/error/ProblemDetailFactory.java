package com.dochiri.tooltestlab.global.error;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Locale;

@Component
public class ProblemDetailFactory {

    private final String typeUriPrefix;

    public ProblemDetailFactory(
            @Value("${app.error.type-uri-prefix:https://api.example.com/problems/}")
            String typeUriPrefix
    ) {
        this.typeUriPrefix = typeUriPrefix;
    }

    public ProblemDetail create(
            HttpStatus status,
            String code,
            String title,
            String detail,
            HttpServletRequest request
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                status,
                detail
        );

        problemDetail.setTitle(title);
        problemDetail.setType(toTypeUri(code));
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        problemDetail.setProperty("code", code);
        problemDetail.setProperty("timestamp", OffsetDateTime.now().toString());

        String traceId = MDC.get("traceId");
        if (traceId != null) {
            problemDetail.setProperty("traceId", traceId);
        }

        return problemDetail;
    }

    public ProblemDetail createSimple(
            HttpStatus status,
            String code,
            String title,
            String detail
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                status,
                detail
        );

        problemDetail.setTitle(title);
        problemDetail.setType(toTypeUri(code));

        problemDetail.setProperty("code", code);
        problemDetail.setProperty("timestamp", OffsetDateTime.now().toString());

        String traceId = MDC.get("traceId");
        if (traceId != null) {
            problemDetail.setProperty("traceId", traceId);
        }

        return problemDetail;
    }

    private URI toTypeUri(String code) {
        String path = code.toLowerCase(Locale.ROOT).replace("_", "-");
        return URI.create(typeUriPrefix + path);
    }

}
