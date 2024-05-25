package jjapra.app.controller;

import jakarta.servlet.http.HttpSession;
import jjapra.app.config.jwt.JwtProvider;
import jjapra.app.dto.member.AddMemberRequest;
import jjapra.app.model.member.Member;
import jjapra.app.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.coyote.Response;
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

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<Object> displayJoinSuccessPage(@RequestBody AddMemberRequest request) {
        if (request.getUsername().isEmpty()){
            return ResponseEntity.badRequest().body(null);
        }
        if (memberService.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("already exists id");
        }

        memberService.save(request);
        return ResponseEntity.ok().body("success");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println(request.getUsername() + " " + request.getPassword());
        Optional<Member> member = memberService.findByUsername(request.getUsername());
        if (member.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid id");
        }
        if (member.get().getPassword().equals(request.getPassword())) {
            String token = jwtProvider.createJwt(member.get().getUsername(), member.get().getRole().toString());
            return ResponseEntity.status(HttpStatus.OK).body(token);
        } else {
            return ResponseEntity.badRequest().body("Invalid password");
        }
    }

    // 회원 정보 조회. 회원 ID를 받아서 해당 회원의 정보를 반환
    @GetMapping("/members/{username}")
    public Optional<Member> getMember(@PathVariable("username") String username) {
        return memberService.findByUsername(username);
    }

    @GetMapping("/members")
    public List<Member> getMembers() {
        return memberService.findAll();
    }
}

@Getter
@Setter
@EnableWebMvc
class LoginRequest {
    private String username;
    private String password;
}