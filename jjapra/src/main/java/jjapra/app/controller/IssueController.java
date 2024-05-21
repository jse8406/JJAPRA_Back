package jjapra.app.controller;

import jakarta.servlet.http.HttpSession;

import jjapra.app.dto.AddCommentRequest;
import jjapra.app.dto.AddIssueRequest;
import jjapra.app.dto.IssueDetailsResponse;
import jjapra.app.model.Comment;
import jjapra.app.model.Issue;
import jjapra.app.model.Member;
import jjapra.app.model.ProjectMember;

import jjapra.app.service.IssueService;
import jjapra.app.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class IssueController {
    private final IssueService issueService;
    private final ProjectMemberService projectMemberService;

    @PostMapping("/issues")
    public ResponseEntity<Issue> addIssue(@RequestBody AddIssueRequest request, HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        request.setWriter(loggedInUser.getId());
        Issue savedIssue = issueService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedIssue);
    }

    @GetMapping("/issues")
    public ResponseEntity<List<Issue>> getIssues(HttpSession session) {
        Member loggedInUser = (Member) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<ProjectMember> projectMemberList = projectMemberService.findByMemberId(loggedInUser.getId());
        List<Integer> projectIds = projectMemberList.stream()
                .map(pm -> pm.getProject().getId())
                .collect(Collectors.toList());

        List<Issue> allIssues = issueService.findAll();

        List<Issue> filteredIssues = allIssues.stream()
                .filter(issue -> projectIds.contains(issue.getProjectId()))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(filteredIssues);
    }

    @GetMapping("/issues/details/{id}")
    public ResponseEntity<IssueDetailsResponse> getIssueDetails(@PathVariable("id") Integer id, HttpSession session) {

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
