package jjapra.app.controller;

import jjapra.app.config.jwt.JwtMember;
import jjapra.app.dto.issue.AddIssueMemberRequest;
import jjapra.app.model.issue.Issue;
import jjapra.app.model.issueMember.IssueAssignee;
import jjapra.app.model.member.Member;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Map;
import java.util.Optional;

@EnableWebMvc
@RequiredArgsConstructor
@RestController
@RequestMapping("/issues")
public class IssueMemberController {
    private final IssueAssigneeService issueAssigneeService;
    private final IssueService issueService;
    private final MemberService memberService;
    private final ProjectService projectService;
    private final ProjectMemberService projectMemberService;
    private final JwtMember jwtMember;
    @PostMapping("/{issueId}/members")
    public ResponseEntity<?> save(@PathVariable("issueId") Integer issueId,
                                  @RequestBody Map<String, String> request,
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
        if (projectMember.isPresent() && (projectMember.get().getRole().toString().equals("PL")
                || projectMember.get().getRole().toString().equals("ADMIN"))) {
            Optional<Member> member = memberService.findById(request.get("id"));
            if (member.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            issueAssigneeService.findByIssueId(issueId).ifPresent(issueAssigneeService::delete);
            return ResponseEntity.status(HttpStatus.CREATED).body(issueAssigneeService
                    .save(IssueAssignee.builder().issue(issue.get()).member(member.get()).build()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}