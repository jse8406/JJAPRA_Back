package jjapra.app.controller;


import jakarta.servlet.http.HttpSession;
import jjapra.app.dto.AddIssueRequest;
import jjapra.app.model.Issue;
import jjapra.app.model.Member;
import jjapra.app.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jjapra.app.service.JJapraService;

// 로그인 컨트롤러
@RequiredArgsConstructor
@Controller
public class LoginController {

    private final MemberRepository memberRepository;
    private final JJapraService jJapraService;


    //Create issue endpoint
    @PostMapping("/api/issues")
    public ResponseEntity<Issue> addIssue(@ModelAttribute AddIssueRequest request, HttpSession session){
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        request.setWriter(loggedInUser.getId());

        Issue savedIssue = jJapraService.save(request);
        System.out.println("Created");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedIssue);
    }

    //login check api
    @PostMapping("/login")
    public String login(@RequestParam String id, @RequestParam String password, HttpSession session) {
        Member member = memberRepository.findById(id).orElse(null);
        if (member != null && member.getPassword().equals(password)) {
            session.setAttribute("loggedInUser", member);
            printSession(session);
            return "redirect:/success"; // 로그인 성공 후 메인 페이지로 이동. 현재는 임시로 만들어 놓은 success 페이지로 이동
        } else {

            return "redirect:/"; // 로그인 실패 시 다시 로그인 페이지로 이동
        }
    }



    @RequestMapping(value = {"/success"}, method = RequestMethod.GET)
    public String displayHomePage(HttpSession session){
        printSession(session);
        return "success";}

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String displayLoginPage(HttpSession session){
        printSession(session);
        return "test";}



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
}
