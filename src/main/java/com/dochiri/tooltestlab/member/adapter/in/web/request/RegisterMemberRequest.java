package com.dochiri.tooltestlab.member.adapter.in.web.request;

import com.dochiri.tooltestlab.member.application.port.in.command.RegisterMemberCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "회원 가입 요청")
public record RegisterMemberRequest(
        @Schema(description = "이메일 주소", example = "user@example.com")
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,
        @Schema(description = "비밀번호", example = "password123")
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,
        @Schema(description = "이름", example = "홍길동")
        @NotBlank(message = "이름은 필수입니다.")
        String name) {

    public RegisterMemberCommand toCommand() {
        return new RegisterMemberCommand(email, password, name);
    }
}
