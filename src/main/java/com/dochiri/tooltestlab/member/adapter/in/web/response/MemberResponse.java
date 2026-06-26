package com.dochiri.tooltestlab.member.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 응답")
public record MemberResponse(
        @Schema(description = "회원 publicId", example = "a1b2c3d4e5f6") String publicId,
        @Schema(description = "이메일 주소", example = "user@example.com") String email,
        @Schema(description = "이름", example = "홍길동") String name) {
}
