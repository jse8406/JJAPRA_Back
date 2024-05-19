package jjapra.app.controller;

import jjapra.app.dto.AddMemberRequest;
import jjapra.app.model.Member;
import jjapra.app.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @RequestMapping(value = "/join")
    public String displayJoinPage() {
        return "join.html";
    }

    @PostMapping(value = "/join")
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

    @RequestMapping(value = "/member/{id}", method = RequestMethod.GET)
    public Member getMember(@PathVariable("id") String id) {
        return memberService.findById(id);
    }
}
