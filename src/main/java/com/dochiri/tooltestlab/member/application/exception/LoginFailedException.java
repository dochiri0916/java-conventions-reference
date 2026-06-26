package com.dochiri.tooltestlab.member.application.exception;

import com.dochiri.tooltestlab.global.error.BusinessException;

public class LoginFailedException extends BusinessException {

    public LoginFailedException() {
        super(MemberErrorCode.LOGIN_FAILED);
    }

    public LoginFailedException(String message) {
        super(MemberErrorCode.LOGIN_FAILED, message);
    }

}
