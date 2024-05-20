package jjapra.app.controller;

import jakarta.servlet.http.HttpSession;
import jjapra.app.dto.AddIssueRequest;
import jjapra.app.dto.IssueListResponse;
import jjapra.app.model.Comment;
import jjapra.app.model.Issue;
import jjapra.app.model.Member;
import jjapra.app.service.IssueService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class IssueController {
    private final IssueService issueService;

    @PostMapping("/issues")
    public ResponseEntity<Issue> addIssue(@ModelAttribute AddIssueRequest request, HttpSession session) {
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

    @GetMapping("/issues/details/{id}")
    public String getIssueDetails(@PathVariable("id") Integer id, Model model) {
        Issue issue = issueService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue Id:" + id));
        List<Comment> comments = issueService.findCommentsByIssueId(id);
        model.addAttribute("issue", issue); //thymeleaf에서 해당 model로 item 설정
        model.addAttribute("comments", comments);
        return "issueDetails"; // issueDetails.html 템플릿으로 렌더링
    }

    @PostMapping("/issues/details/{id}/addComment")
    public String addComment(@PathVariable("id") Integer id,
                             @RequestParam String content,
                             HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        String writerId = loggedInUser.getId();
        issueService.addComment(id, writerId, content);
        return "redirect:/issues/details/" + id;
    }

    @GetMapping("/projects/{projectId}/issues")
    public List<Issue> getIssuesByProjectId(@PathVariable("projectId") Integer projectId) {
        return issueService.findByProjectId(projectId);
    }
}
