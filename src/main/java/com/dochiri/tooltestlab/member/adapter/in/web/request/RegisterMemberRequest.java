package com.dochiri.tooltestlab.member.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 가입 요청")
public record RegisterMemberRequest(
        @Schema(description = "이메일 주소", example = "user@example.com") String email,
        @Schema(description = "비밀번호", example = "password123") String password,
        @Schema(description = "이름", example = "홍길동") String name) {
}
