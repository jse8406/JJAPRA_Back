package jjapra.app.controller;

import jjapra.app.model.Member;
import jjapra.app.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @RequestMapping(value = "/member/{id}", method = RequestMethod.GET)
    public Member getMember(@PathVariable("id") String id) {
        return memberService.findById(id);
    }
}
