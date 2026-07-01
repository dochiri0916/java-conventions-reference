package com.dochiri.tooltestlab.member.adapter.in.web;

import com.dochiri.tooltestlab.member.adapter.in.web.request.LoginMemberRequest;
import com.dochiri.tooltestlab.member.adapter.in.web.request.RegisterMemberRequest;
import com.dochiri.tooltestlab.member.adapter.in.web.response.MemberResponse;
import com.dochiri.tooltestlab.member.application.port.in.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "회원", description = "회원 가입, 로그인, 로그아웃 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final RegisterMemberUseCase registerMemberUseCase;
    private final LoginMemberUseCase loginMemberUseCase;

    @Operation(summary = "회원 가입", description = "새로운 회원을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일, 비밀번호, 또는 이름"),
            @ApiResponse(responseCode = "409", description = "이미 등록된 이메일")
    })
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> register(@Valid @RequestBody RegisterMemberRequest request) {
        final RegisterMemberResult result = registerMemberUseCase.register(
                new RegisterMemberCommand(request.email(), request.password(), request.name()));
        return ResponseEntity.ok(new MemberResponse(result.publicId(), result.email(), result.name()));
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 또는 비밀번호 형식"),
            @ApiResponse(responseCode = "401", description = "이메일 또는 비밀번호 불일치")
    })
    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(
            @Valid @RequestBody LoginMemberRequest request, final HttpSession session) {
        final LoginMemberResult result = loginMemberUseCase.login(
                new LoginMemberCommand(request.email(), request.password()));
        session.setAttribute(LoginCheckInterceptor.MEMBER_ID_KEY, result.publicId());
        return ResponseEntity.ok(new MemberResponse(result.publicId(), result.email(), result.name()));
    }

    @Operation(summary = "로그아웃", description = "현재 세션을 무효화하고 로그아웃합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    })
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(final HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "로그아웃 되었습니다."));
    }

    @Operation(summary = "내 정보 조회", description = "로그인된 회원의 publicId를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청")
    })
    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me(@Parameter(hidden = true) @LoginMember String memberPublicId) {
        return ResponseEntity.ok(Map.of("publicId", memberPublicId));
    }
}
