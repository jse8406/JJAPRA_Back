package jjapra.app.controller;

import jakarta.servlet.http.HttpSession;
import jjapra.app.dto.AddIssueRequest;
import jjapra.app.dto.IssueListResponse;
import jjapra.app.model.Issue;
import jjapra.app.model.Member;
import jjapra.app.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class IssueController {
    private final IssueService issueService;

    @PostMapping("/api/issues")
    public ResponseEntity<Issue> addIssue(@ModelAttribute AddIssueRequest request, HttpSession session){
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        request.setWriter(loggedInUser.getId());

        Issue savedIssue = issueService.save(request);
        System.out.println("Created");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedIssue);
    }

    @GetMapping("/issues")
    public String getIssues(Model model) {
        List<IssueListResponse> issues = issueService.findAll().stream()
                .map(IssueListResponse::new)
                .toList();
        model.addAttribute("issue", issues);
        return "issueList";
    }

    @GetMapping("/issue/details/{id}")
    public String getIssueDetails(@PathVariable("id") Integer id, Model model) {
        Issue issue = issueService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue Id:" + id));
        model.addAttribute("issue", issue);
        return "issueDetails"; // issueDetails.html 템플릿으로 렌더링
    }
}
