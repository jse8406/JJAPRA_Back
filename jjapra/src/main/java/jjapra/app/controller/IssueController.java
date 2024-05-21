package jjapra.app.controller;

import jakarta.servlet.http.HttpSession;
import jjapra.app.dto.AddCommentRequest;
import jjapra.app.dto.AddIssueRequest;
import jjapra.app.dto.IssueDetailsResponse;
import jjapra.app.dto.IssueListResponse;
import jjapra.app.model.Comment;
import jjapra.app.model.Issue;
import jjapra.app.model.Member;
import jjapra.app.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class IssueController {
    private final IssueService issueService;

    @PostMapping("/issues")
    public ResponseEntity<Issue> addIssue(@RequestBody AddIssueRequest request, HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        request.setWriter(loggedInUser.getId());
        Issue savedIssue = issueService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedIssue);
    }

    @GetMapping("/issues")
    public ResponseEntity<List<IssueListResponse>> getIssues() {
        List<IssueListResponse> issues = issueService.findAll().stream()
                .map(IssueListResponse::new)
                .toList();
        return ResponseEntity.status(HttpStatus.OK)
                .body(issues);
    }

    @GetMapping("/issues/details/{id}")
    public ResponseEntity<IssueDetailsResponse> getIssueDetails(@PathVariable("id") Integer id) {
        Issue issue = issueService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue Id:" + id));
        List<Comment> comments = issueService.findCommentsByIssueId(id);

        IssueDetailsResponse response = new IssueDetailsResponse(issue.getTitle(), issue.getDescription(), comments);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/comments/{issueId}")
    public ResponseEntity<Comment> addComment(@PathVariable("issueId") Integer issueId ,
                                              @RequestBody AddCommentRequest request,
                                              HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        request.setWriterId(loggedInUser.getId());
        Comment savedComment = issueService.addComment(issueId,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @GetMapping("/projects/{projectId}/issues")
    public List<Issue> getIssuesByProjectId(@PathVariable("projectId") Integer projectId) {
        return issueService.findByProjectId(projectId);
    }
}
