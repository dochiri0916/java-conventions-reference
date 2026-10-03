package com.dochiri.tooltestlab.member.adapter.out.security;

import com.dochiri.tooltestlab.member.application.port.out.PasswordEncoderPort;
import com.dochiri.tooltestlab.member.domain.vo.EncodedPassword;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
class BCryptPasswordEncoderAdapter implements PasswordEncoderPort {

    @Override
    public EncodedPassword encode(final String rawPassword) {
        return new EncodedPassword(new BCryptPasswordEncoder().encode(rawPassword));
    }

    @Override
    public boolean matches(final String rawPassword, final EncodedPassword encodedPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword.value());
    }
}
