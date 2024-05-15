package jjapra.app.controller;


import jakarta.servlet.http.HttpSession;
import jjapra.app.dto.AddIssueRequest;
import jjapra.app.model.Issue;
import jjapra.app.model.Member;
import jjapra.app.repository.IssueRepository;
import jjapra.app.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jjapra.app.service.JJapraService;

// 로그인 컨트롤러
@RequiredArgsConstructor
@RestController
public class LoginController {

    private MemberRepository memberRepository;
    private final JJapraService jJapraService;

    @PostMapping("/api/issues")
    public ResponseEntity<Issue> addIssue(@RequestBody AddIssueRequest request){
        Issue savedIssue = jJapraService.save(request);
        System.out.println("Created");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedIssue);

    }

    @RequestMapping(value = {"/success"})
    public String displayHomePage(){return "success.html";}

    @RequestMapping(value = "/")
    public String displayLoginPage(){return "test.html";}

    @PostMapping("/login")
    public String login(@RequestParam String id, @RequestParam String password, HttpSession session) {
        Member member = memberRepository.findById(id).orElse(null);
        System.out.println(member);
        if (member != null && member.getPassword().equals(password)) {
            session.setAttribute("loggedInUser", member);
            return "redirect:/success"; // 로그인 성공 후 메인 페이지로 이동
        } else {

            return "redirect:/"; // 로그인 실패 시 다시 로그인 페이지로 이동
        }
    }
}
