package com.dochiri.tooltestlab.global.error;

import org.springframework.http.HttpStatus;

public final class ApiErrorStatusMapper {

    private ApiErrorStatusMapper() {

    }

    public static HttpStatus toStatus(ErrorCode errorCode) {
        return errorCode.httpStatus();
    }

}
