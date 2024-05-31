package jjapra.app.controller;

import jjapra.app.config.jwt.JwtMember;
import jjapra.app.dto.issue.AddCommentRequest;
import jjapra.app.dto.issue.AddIssueRequest;
import jjapra.app.dto.issue.UpdateIssueRequest;
import jjapra.app.model.issue.Comment;
import jjapra.app.model.issue.Issue;
import jjapra.app.model.issueMember.IssueAssignee;
import jjapra.app.model.issueMember.IssueFixer;
import jjapra.app.model.member.Member;
import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.response.IssueDetailsResponse;
import jjapra.app.service.*;
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
    private final IssueAssigneeService issueAssigneeService;
    private final IssueFixerService issueFixerService;

    @PostMapping("/projects/{projectId}/issues")
    public ResponseEntity<Issue> addIssue(@RequestBody AddIssueRequest request, @PathVariable("projectId") Integer projectId,
                                          @RequestHeader("Authorization") String token) {
        Optional<Member> loggedInUser = jwtMember.getMember(token);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<Project> project = projectService.findById(projectId);
        if (projectMemberService.findByProjectAndMember(project.get(), loggedInUser.get()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        request.setProjectId(projectId);
        request.setWriter(loggedInUser.get().getId());
        Issue savedIssue = issueService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedIssue);
    }

    @GetMapping("/issues")
    public ResponseEntity<List<Issue>> getIssueList(@RequestHeader("Authorization") String token) {
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

    @GetMapping("/issues/{issueId}")
    public ResponseEntity<IssueDetailsResponse> getIssueDetails(@PathVariable("issueId") Integer issueId,
                                                                @RequestHeader("Authorization") String token) {
        Optional<Member> loggedInUser = jwtMember.getMember(token);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Optional<Issue> issue = issueService.findById(issueId);
        if (issue.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Optional<ProjectMember> pm = projectMemberService.findByProjectAndMember(
                projectService.findById(issue.get().getProjectId()).get(), loggedInUser.get());
        if (pm.isPresent()) {
            Optional<IssueAssignee> issueAssignee = issueAssigneeService.findByIssueId(issueId);
            Optional<IssueFixer> issueFixer = issueFixerService.findByIssueId(issueId);
            IssueDetailsResponse response = new IssueDetailsResponse(issue.get());
            if(issueAssignee.isPresent()) {
                response.setAssignee(issueAssignee.get().getMember().getId());
            }
            if (issueFixer.isPresent()) {
                response.setFixer(issueFixer.get().getMember().getId());
            }
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            // Handle the case where either the issueAssignee or issueFixer is not present
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/issues/{issueId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable("issueId") Integer issueId ,
                                              @RequestBody AddCommentRequest request,
                                              @RequestHeader("Authorization") String token) {
        Optional<Member> loggedInUser = jwtMember.getMember(token);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<Issue> issue = issueService.findById(issueId);
        if (issue.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<ProjectMember> pm = projectMemberService.findByProjectAndMember(
                projectService.findById(issue.get().getProjectId()).get(), loggedInUser.get());
        if (pm.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Comment savedComment = issueService.addComment(issueId, request, loggedInUser.get().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @GetMapping("/projects/{projectId}/issues")
    public ResponseEntity<List<IssueDetailsResponse>> getIssuesByProjectId(@PathVariable("projectId") Integer projectId,
                                            @RequestHeader("Authorization") String token) {
        Optional<Member> loggedInUser = jwtMember.getMember(token);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (projectMemberService.findByProjectAndMember(
                projectService.findById(projectId).get(), loggedInUser.get()).isPresent()) {
            List<Issue> issueList = issueService.findByProjectId(projectId);
            List<IssueDetailsResponse> responseList = issueList.stream()
                    .map(issue -> {
                        Optional<IssueAssignee> issueAssignee = issueAssigneeService.findByIssueId(issue.getIssueId());
                        Optional<IssueFixer> issueFixer = issueFixerService.findByIssueId(issue.getIssueId());
                        IssueDetailsResponse response = new IssueDetailsResponse(issue);
                        if(issueAssignee.isPresent()) {
                            response.setAssignee(issueAssignee.get().getMember().getId());
                        }
                        if (issueFixer.isPresent()) {
                            response.setFixer(issueFixer.get().getMember().getId());
                        }
                        return response;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responseList);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    //issue 수정
    @PatchMapping("/issues/{issueId}")
    public ResponseEntity<Issue> updateIssue(@PathVariable("issueId") Integer issueId, @RequestBody UpdateIssueRequest request,
                                             @RequestHeader("Authorization") String token) {
        Optional<Member> loggedInUser = jwtMember.getMember(token);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<Issue> issue = issueService.findById(issueId);
        if (issue.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (projectMemberService.findByProjectAndMember(
                projectService.findById(issue.get().getProjectId()).get(), loggedInUser.get()).isPresent()) {
            Issue updatedIssue = issueService.updateIssue(issueId, request);
            return ResponseEntity.ok(updatedIssue);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    //issue 삭제
    @DeleteMapping("/issues/{issueId}")
    public ResponseEntity<Void> deleteIssue(@PathVariable("issueId") Integer issueId,
                                            @RequestHeader("Authorization") String token) {
        Optional<Member> loggedInUser = jwtMember.getMember(token);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<Issue> issue = issueService.findById(issueId);
        if (issue.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<ProjectMember> pm = projectMemberService.findByProjectAndMember(
                projectService.findById(issue.get().getProjectId()).get(), loggedInUser.get());
        if (pm.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (!loggedInUser.get().getRole().toString().equals("ADMIN") &&
            !pm.get().getRole().toString().equals("PL")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        issueService.deleteIssue(issueId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
