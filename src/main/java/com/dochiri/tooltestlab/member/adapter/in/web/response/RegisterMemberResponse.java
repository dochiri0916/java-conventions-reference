package com.dochiri.tooltestlab.member.adapter.in.web.response;

import com.dochiri.tooltestlab.member.application.port.in.result.RegisterMemberResult;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 등록 응답")
public record RegisterMemberResponse(
        @Schema(description = "회원 publicId", example = "a1b2c3d4e5f6") String publicId,
        @Schema(description = "이메일 주소", example = "user@example.com") String email,
        @Schema(description = "이름", example = "홍길동") String name) {

    public static RegisterMemberResponse from(final RegisterMemberResult result) {
        return new RegisterMemberResponse(result.publicId(), result.email(), result.name());
    }
}
