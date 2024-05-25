package jjapra.app.controller;

import jakarta.servlet.http.HttpSession;
import jjapra.app.config.jwt.JwtMember;
import jjapra.app.dto.issue.AddCommentRequest;
import jjapra.app.dto.issue.AddIssueRequest;
import jjapra.app.dto.issue.IssueDetailsResponse;
import jjapra.app.dto.issue.UpdateIssueRequest;
import jjapra.app.model.issue.Comment;
import jjapra.app.model.issue.Issue;
import jjapra.app.model.member.Member;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.service.IssueService;
import jjapra.app.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

@EnableWebMvc
@RequiredArgsConstructor
@RestController
public class IssueController {
    private final IssueService issueService;
    private final ProjectMemberService projectMemberService;
    private final JwtMember jwtMember;

    @PostMapping("projects/{projectId}/issues")
    public ResponseEntity<Issue> addIssue(@RequestBody AddIssueRequest request, @PathVariable("projectId") Integer projectId,
                                          @RequestHeader("Authorization") String token, HttpSession session) {
        Optional<Member> loggedInUser = jwtMember.getMember(token);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        request.setProjectId(projectId);
        request.setWriter(loggedInUser.get().getId());
        Issue savedIssue = issueService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedIssue);
    }

    @GetMapping("/issues")
    public ResponseEntity<List<Issue>> getIssues(@RequestHeader("Authorization") String token) {
        Optional<Member> loggedInUser = jwtMember.getMember(token);

        List<ProjectMember> projectMemberList = projectMemberService.findByMemberId(loggedInUser.get().getId());

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
                                              @RequestHeader("Authorization") String token) {
        Optional<Member> loggedInUser = jwtMember.getMember(token);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        request.setWriterId(loggedInUser.get().getId());
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
