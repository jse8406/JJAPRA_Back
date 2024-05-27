package jjapra.app.controller;

import jjapra.app.config.jwt.JwtMember;
import jjapra.app.config.jwt.JwtProvider;
import jjapra.app.dto.member.AddMemberRequest;
import jjapra.app.model.member.Member;
import jjapra.app.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@EnableWebMvc
public class MemberController {
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final JwtMember jwtMember;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<Object> displayJoinSuccessPage(@RequestBody AddMemberRequest request) {
        if (request.getId().isEmpty()){
            return ResponseEntity.badRequest().body(null);
        }
        if (memberService.findById(request.getId()).isPresent()) {
            return ResponseEntity.badRequest().body("already exists id");
        }

        memberService.save(request);
        return ResponseEntity.ok().body("success");
    }

    // 로그인
    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Member> member = memberService.findById(request.getId());
        if (member.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid id");
        }
        if (member.get().getPassword().equals(request.getPassword())) {
            String token = jwtProvider.createJwt(member.get().getId(), member.get().getRole().toString());
            LoginResponse loginResponse = new LoginResponse(token, member.get());
            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
        } else {
            return ResponseEntity.badRequest().body("Invalid password");
        }
    }

    // 회원 정보 조회. 회원 ID를 받아서 해당 회원의 정보를 반환
    @GetMapping("/members/{id}")
    public ResponseEntity<Member> getMember(@PathVariable("id") String id,
                                      @RequestHeader("Authorization") String token) {
        Optional<Member> member = jwtMember.getMember(token);
        if (member.isEmpty() || !member.get().getRole().toString().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(memberService.findById(id).orElse(null));
    }

    @GetMapping("/members")
    public ResponseEntity<?> getMembers(@RequestHeader("Authorization") String token) {
        Optional<Member> member = jwtMember.getMember(token);
        if (member.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if (member.get().getRole().toString().equals("ADMIN")) {
            return ResponseEntity.ok(memberService.findAll());
        }

        return ResponseEntity.status(HttpStatus.OK).body(member.get());
    }
}

@Getter
@Setter
@EnableWebMvc
class LoginRequest {
    private String id;
    private String password;
}

@Getter
@RequiredArgsConstructor
class LoginResponse {
    private final String token;
    private final Member member;
}