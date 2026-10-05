package com.dochiri.tooltestlab.member.adapter.in.web.response;

import com.dochiri.tooltestlab.member.application.port.in.result.LoginMemberResult;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답")
public record LoginMemberResponse(
        @Schema(description = "회원 publicId", example = "a1b2c3d4e5f6") String publicId,
        @Schema(description = "이메일 주소", example = "user@example.com") String email,
        @Schema(description = "이름", example = "홍길동") String name) {

    public static LoginMemberResponse from(final LoginMemberResult result) {
        return new LoginMemberResponse(result.publicId(), result.email(), result.name());
    }
}
