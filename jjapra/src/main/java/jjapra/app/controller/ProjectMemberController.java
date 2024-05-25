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

    @PostMapping("/projects/{id}")
    public ResponseEntity<ProjectMember> save(@PathVariable("id") Integer id, @RequestBody AddProjectMemberRequest request,
                                              @RequestHeader("Authorization") String token) {
        Optional<Project> project = projectService.findById(id);
        if (project.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<Member> member = jwtMember.getMember(token);
        if (member.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        ProjectMember entity = request.toEntity(project.get(), member.get(), request.getRole());
        projectMemberService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
