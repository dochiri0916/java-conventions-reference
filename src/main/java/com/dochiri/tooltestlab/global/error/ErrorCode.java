package com.dochiri.tooltestlab.global.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String code();

    String message();

    HttpStatus httpStatus();

}
