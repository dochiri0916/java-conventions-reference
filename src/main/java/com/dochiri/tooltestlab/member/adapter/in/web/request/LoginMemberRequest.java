package com.dochiri.tooltestlab.member.adapter.in.web.request;

import com.dochiri.tooltestlab.member.application.port.in.command.LoginMemberCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "로그인 요청")
public record LoginMemberRequest(
        @Schema(description = "이메일 주소", example = "user@example.com")
        @NotBlank(message = "이메일은 필수입니다.")
        String email,
        @Schema(description = "비밀번호", example = "password123")
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password) {

    public LoginMemberCommand toCommand() {
        return new LoginMemberCommand(email, password);
    }
}
