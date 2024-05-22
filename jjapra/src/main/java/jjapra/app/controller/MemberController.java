package jjapra.app.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jjapra.app.dto.member.AddMemberRequest;
import jjapra.app.model.member.Member;
import jjapra.app.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@RequiredArgsConstructor
@RestController
@EnableWebMvc
public class MemberController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<Object> displayJoinSuccessPage(@RequestBody AddMemberRequest request) {
        if (request.getId().isEmpty()){
            return ResponseEntity.badRequest().body(null);
        }
        if (memberService.findById(request.getId()) != null) {
            return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다.");
        }

        memberService.save(request);
        return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session, HttpServletResponse response) {
        Member member = memberService.findById(request.getId());
        if (member == null) {
            return ResponseEntity.badRequest().body("아이디 오류");
        }
        if (member.getPassword().equals(request.getPassword())) {
            session.setAttribute("loggedInUser", member);
            Cookie cookie = new Cookie("memberId", member.getId());
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24); // 1일 동안 유효
            response.addCookie(cookie);
            return ResponseEntity.ok().body("loggin success");
        } else {
            return ResponseEntity.badRequest().body("비밀번호 오류");
        }
    }

    // 회원 정보 조회. 회원 ID를 받아서 해당 회원의 정보를 반환
    @GetMapping("/members/{id}")
    public Member getMember(@PathVariable("id") String id) {
        return memberService.findById(id);
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
    private String id;
    private String password;
}