package jjapra.app.controller;

import jjapra.app.config.jwt.JwtMember;
import jjapra.app.dto.issue.AddIssueMemberRequest;
import jjapra.app.model.issue.Issue;
import jjapra.app.model.issueMember.IssueAssignee;
import jjapra.app.model.issueMember.IssueFixer;
import jjapra.app.model.member.Member;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Optional;

@EnableWebMvc
@RequiredArgsConstructor
@RestController
@RequestMapping("/issues")
public class IssueMemberController {
    private final IssueFixerService issueFixerService;
    private final IssueAssigneeService issueAssigneeService;
    private final IssueService issueService;
    private final MemberService memberService;
    private final ProjectService projectService;
    private final ProjectMemberService projectMemberService;
    private final JwtMember jwtMember;
    @PostMapping("/{issueId}/members")
    public ResponseEntity<?> save(@PathVariable("issueId") Integer issueId,
                                  @RequestBody AddIssueMemberRequest request,
                                  @RequestHeader("Authorization") String token) {

        Optional<Member> loggedInUser = jwtMember.getMember(token);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<Issue> issue = issueService.findById(issueId);
        if (issue.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<ProjectMember> projectMember = projectMemberService.findByProjectAndMember(
                projectService.findById(issueService.findById(issueId).get().getProjectId()).get(),
                loggedInUser.get());
        if (projectMember.isEmpty() || !projectMember.get().getRole().toString().equals("PL")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<Member> member = memberService.findById(request.getId());
        if (member.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (request.getRole().equals("FIXER")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(issueFixerService
                    .save(IssueFixer.builder().issue(issue.get()).member(member.get()).build()));
        } else if (request.getRole().equals("ASSIGNEE")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(issueAssigneeService
                    .save(IssueAssignee.builder().issue(issue.get()).member(member.get()).build()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
