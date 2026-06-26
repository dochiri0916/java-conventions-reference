package com.dochiri.tooltestlab.member.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청")
public record LoginMemberRequest(
        @Schema(description = "이메일 주소", example = "user@example.com") String email,
        @Schema(description = "비밀번호", example = "password123") String password) {
}
