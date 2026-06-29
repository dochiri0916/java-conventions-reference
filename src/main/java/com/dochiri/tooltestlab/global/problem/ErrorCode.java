package com.dochiri.tooltestlab.global.problem;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    HttpStatus status();

    String code();

    String type();

    String title();

}
