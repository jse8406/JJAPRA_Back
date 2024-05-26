package jjapra.app.controller;

import jjapra.app.config.jwt.JwtMember;
import jjapra.app.dto.project.AddProjectMemberRequest;
import jjapra.app.model.member.Member;
import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.service.MemberService;
import jjapra.app.service.ProjectMemberService;
import jjapra.app.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Optional;
@EnableWebMvc
@RequiredArgsConstructor
@RestController
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;
    private final ProjectService projectService;
    private final MemberService memberService;
    private final JwtMember jwtMember;

    @PostMapping("/projects/{projectId}/members")
    public ResponseEntity<ProjectMember> save(@PathVariable("projectId") Integer id, @RequestBody AddProjectMemberRequest request,
                                              @RequestHeader("Authorization") String token) {

        if (!jwtMember.getMember(token).get().getRole().toString().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Optional<Project> project = projectService.findById(id);
        if (project.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<Member> member = memberService.findById(request.getId());
        if (member.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        ProjectMember entity = AddProjectMemberRequest.toEntity(project.get(), member.get(), request.getRole());
        ProjectMember projectMember = projectMemberService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectMember);
    }
}
