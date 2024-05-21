package jjapra.app.controller;

import jakarta.servlet.http.HttpSession;
import jjapra.app.dto.issue.AddCommentRequest;
import jjapra.app.dto.issue.AddIssueRequest;
import jjapra.app.dto.issue.IssueDetailsResponse;
import jjapra.app.dto.issue.UpdateIssueRequest;
import jjapra.app.model.issue.Comment;
import jjapra.app.model.issue.Issue;
import jjapra.app.model.member.Member;
import jjapra.app.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
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
    public ResponseEntity<List<Issue>> getIssues() {
        List<Issue> issues = issueService.findAll();
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

    //issue 수정
    @PatchMapping("/issues/{id}")
    public ResponseEntity<Issue> updateIssue(@PathVariable("id") Integer id, @RequestBody UpdateIssueRequest request) {
        Issue updatedIssue = issueService.updateIssue(id, request);
        return ResponseEntity.ok(updatedIssue);
    }
    //issue 삭제
    @DeleteMapping("/issues/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable("id") Integer id) {
        issueService.deleteIssue(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
