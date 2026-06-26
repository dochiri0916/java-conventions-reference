package com.dochiri.tooltestlab.member.adapter.in.web;

import com.dochiri.tooltestlab.global.web.LoginCheckInterceptor;
import com.dochiri.tooltestlab.global.web.LoginMember;
import com.dochiri.tooltestlab.member.adapter.in.web.request.LoginMemberRequest;
import com.dochiri.tooltestlab.member.adapter.in.web.request.RegisterMemberRequest;
import com.dochiri.tooltestlab.member.adapter.in.web.response.MemberResponse;
import com.dochiri.tooltestlab.member.application.command.LoginMemberCommand;
import com.dochiri.tooltestlab.member.application.command.RegisterMemberCommand;
import com.dochiri.tooltestlab.member.application.port.in.LoginMemberUseCase;
import com.dochiri.tooltestlab.member.application.port.in.RegisterMemberUseCase;
import com.dochiri.tooltestlab.member.application.result.LoginMemberResult;
import com.dochiri.tooltestlab.member.application.result.RegisterMemberResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "회원", description = "회원 가입, 로그인, 로그아웃 API")
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final RegisterMemberUseCase registerMemberUseCase;
    private final LoginMemberUseCase loginMemberUseCase;

    public MemberController(RegisterMemberUseCase registerMemberUseCase, LoginMemberUseCase loginMemberUseCase) {
        this.registerMemberUseCase = registerMemberUseCase;
        this.loginMemberUseCase = loginMemberUseCase;
    }

    @Operation(summary = "회원 가입", description = "새로운 회원을 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> register(@RequestBody RegisterMemberRequest request) {
        RegisterMemberResult result = registerMemberUseCase.register(
                new RegisterMemberCommand(request.email(), request.password(), request.name()));
        return ResponseEntity.ok(new MemberResponse(result.publicId(), result.email(), result.name()));
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(@RequestBody LoginMemberRequest request, HttpSession session) {
        LoginMemberResult result = loginMemberUseCase.login(
                new LoginMemberCommand(request.email(), request.password()));
        session.setAttribute(LoginCheckInterceptor.MEMBER_ID_KEY, result.publicId());
        return ResponseEntity.ok(new MemberResponse(result.publicId(), result.email(), result.name()));
    }

    @Operation(summary = "로그아웃", description = "현재 세션을 무효화하고 로그아웃합니다.")
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "로그아웃 되었습니다."));
    }

    @Operation(summary = "내 정보 조회", description = "로그인된 회원의 publicId를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me(@LoginMember String memberPublicId) {
        return ResponseEntity.ok(Map.of("publicId", memberPublicId));
    }
}
