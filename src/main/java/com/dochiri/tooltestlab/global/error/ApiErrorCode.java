package com.dochiri.tooltestlab.global.error;

import org.springframework.http.HttpStatus;

public interface ApiErrorCode {

    HttpStatus status();

    String code();

    String type();

    String title();
}
