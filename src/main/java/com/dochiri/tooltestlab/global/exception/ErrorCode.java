package com.dochiri.tooltestlab.global.exception;

public interface ErrorCode {

    String code();

    ErrorKind kind();

    String detail();

}
