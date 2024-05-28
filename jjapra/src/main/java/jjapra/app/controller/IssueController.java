package jjapra.app.controller;

import jjapra.app.config.jwt.JwtMember;
import jjapra.app.dto.issue.AddCommentRequest;
import jjapra.app.dto.issue.AddIssueRequest;
import jjapra.app.dto.issue.UpdateIssueRequest;
import jjapra.app.model.issue.Comment;
import jjapra.app.model.issue.Issue;
import jjapra.app.model.member.Member;
import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.service.IssueService;
import jjapra.app.service.ProjectMemberService;
import jjapra.app.service.ProjectService;
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
    private final ProjectService projectService;
    private final JwtMember jwtMember;

    @PostMapping("projects/{projectId}/issues")
    public ResponseEntity<Issue> addIssue(@RequestBody AddIssueRequest request, @PathVariable("projectId") Integer projectId,
                                          @RequestHeader("Authorization") String token) {
        Optional<Member> loggedInUser = jwtMember.getMember(token);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<Project> project = projectService.findById(projectId);
        if (!loggedInUser.get().getRole().toString().equals("ADMIN") &&
            projectMemberService.findByProjectAndMember(project.get(), loggedInUser.get()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
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
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Issue> allIssues = issueService.findAll();

        if(loggedInUser.get().getRole().toString().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.OK).body(allIssues);
        }

        List<ProjectMember> projectMemberList = projectMemberService.findByMemberId(loggedInUser.get().getId());
        List<Integer> projectIds = projectMemberList.stream()
                .map(pm -> pm.getProject().getId())
                .collect(Collectors.toList());

        List<Issue> filteredIssues = allIssues.stream()
                .filter(issue -> projectIds.contains(issue.getProjectId()))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(filteredIssues);
    }

    @GetMapping("/issues/details/{id}")
    public ResponseEntity<Issue> getIssueDetails(@PathVariable("id") Integer id) {
        Issue issue = issueService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue Id:" + id));

        return ResponseEntity.status(HttpStatus.OK).body(issue);
    }

    @PostMapping("/comments/{issueId}")
    public ResponseEntity<Comment> addComment(@PathVariable("issueId") Integer issueId ,
                                              @RequestBody AddCommentRequest request,
                                              @RequestHeader("Authorization") String token) {
        Optional<Member> loggedInUser = jwtMember.getMember(token);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Comment savedComment = issueService.addComment(issueId, request, loggedInUser.get().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @GetMapping("/projects/{projectId}/issues")
    public ResponseEntity<List<Issue>> getIssuesByProjectId(@PathVariable("projectId") Integer projectId,
                                            @RequestHeader("Authorization") String token) {
        Optional<Member> loggedInUser = jwtMember.getMember(token);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (loggedInUser.get().getRole().toString().equals("ADMIN")) {
            return ResponseEntity.ok(issueService.findByProjectId(projectId));
        }

        if (projectMemberService.findByProjectAndMember(
                projectService.findById(projectId).get(), loggedInUser.get()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(issueService.findByProjectId(projectId));
    }

    //issue 수정
    @PatchMapping("/issues/{id}")
    public ResponseEntity<Issue> updateIssue(@PathVariable("id") Integer id, @RequestBody UpdateIssueRequest request,
                                             @RequestHeader("Authorization") String token) {
        Optional<Member> loggedInUser = jwtMember.getMember(token);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<Issue> issue = issueService.findById(id);
        if (issue.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (!loggedInUser.get().getRole().toString().equals("ADMIN") &&
            projectMemberService.findByProjectAndMember(projectService.findById(issue.get().getProjectId()).get(),
                    loggedInUser.get()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Issue updatedIssue = issueService.updateIssue(id, request);
        return ResponseEntity.ok(updatedIssue);
    }

    //issue 삭제
    @DeleteMapping("/issues/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable("id") Integer id, @RequestHeader("Authorization") String token) {
        Optional<Member> loggedInUser = jwtMember.getMember(token);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<Issue> issue = issueService.findById(id);
        if (issue.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (!loggedInUser.get().getRole().toString().equals("ADMIN") &&
            projectMemberService.findByProjectAndMember(projectService.findById(issue.get().getProjectId()).get(),
                    loggedInUser.get()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        issueService.deleteIssue(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
