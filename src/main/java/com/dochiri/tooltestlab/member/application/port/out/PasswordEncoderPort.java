package com.dochiri.tooltestlab.member.application.port.out;

import com.dochiri.tooltestlab.member.domain.vo.EncodedPassword;

public interface PasswordEncoderPort {

    EncodedPassword encode(String rawPassword);

    boolean matches(String rawPassword, EncodedPassword encodedPassword);
}
