package jjapra.app.controller;

import jakarta.servlet.http.HttpSession;
import jjapra.app.dto.AddMemberRequest;
import jjapra.app.model.Member;
import jjapra.app.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
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
    public String login(@RequestBody LoginRequest request, HttpSession session) {
        Member member = memberService.findById(request.getId());
        if (member != null && member.getPassword().equals(request.getPassword())) {
            session.setAttribute("loggedInUser", member);
            printSession(session);
            return "redirect:/success"; // 로그인 성공 후 메인 페이지로 이동. 현재는 임시로 만들어 놓은 success 페이지로 이동
        } else {

            return "redirect:/"; // 로그인 실패 시 다시 로그인 페이지로 이동
        }
    }

    // 세션 정보 출력
    public void printSession(HttpSession session){
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            // 세션에 사용자가 존재하면, 사용자 ID를 출력
            System.out.println("Logged in user ID: " + loggedInUser.getId());
        } else {
            // 세션에 사용자가 존재하지 않으면, 메시지를 출력
            System.out.println("No user is logged in.");
        }
    }

    // 회원 정보 조회. 회원 ID를 받아서 해당 회원의 정보를 반환
    @GetMapping("/members/{id}")
    public Member getMember(@PathVariable("id") String id) {
        return memberService.findById(id);
    }
}

@Getter
@Setter
class LoginRequest {
    private String id;
    private String password;
}