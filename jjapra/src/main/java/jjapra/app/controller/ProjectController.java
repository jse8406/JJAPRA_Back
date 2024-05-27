package jjapra.app.controller;

import jjapra.app.config.jwt.JwtMember;
import jjapra.app.dto.project.AddProjectMemberRequest;
import jjapra.app.dto.project.AddProjectRequest;
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
import java.util.List;
import java.util.Optional;

@EnableWebMvc
@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final JwtMember jwtMember;
    private final ProjectMemberService projectMemberService;
    private final MemberService memberService;

    @GetMapping("")
    public ResponseEntity<List<Project>> getProjects(@RequestHeader("Authorization") String token) {
        Optional<Member> member = jwtMember.getMember(token);
        if (member.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (member.get().getRole().toString().equals("ADMIN")) {
            List<Project> projects = projectService.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(projects);
        }
        
        List<ProjectMember> projectMembers = projectMemberService.findByMemberId(member.get().getId());
        List<Project> projects = projectMembers.stream().map(ProjectMember::getProject).toList();
        projects = projects.stream().distinct().toList();
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }

    @PostMapping("")
    public ResponseEntity<?> addProject(@RequestBody AddProjectRequest request, @RequestHeader("Authorization") String token) {
        Optional<Member> member = jwtMember.getMember(token);
        if (member.isEmpty() || !member.get().getRole().toString().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthorized");
        }

        if (request.getTitle().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("title is required");
        }
        Project project = projectService.findByTitle(request.getTitle());
        if (project != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("already exists title");
        }
        Project savedProject = projectService.save(request);
        projectMemberService.save(AddProjectMemberRequest.toEntity(savedProject, member.get(), "ADMIN"));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable("id") Integer id, @RequestHeader("Authorization") String token) {
        Optional<Member> loggedInUser = jwtMember.getMember(token);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (loggedInUser.get().getRole().toString().equals("ADMIN")) {
            Optional<Project> project = projectService.findById(id);
            if (project.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(project.get());
        }

        Optional<ProjectMember> projectMember = projectMemberService
                .findByProjectAndMember(projectService.findById(id).get(), loggedInUser.get());
        if (projectMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(projectMember.get().getProject());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") Integer id, @RequestBody AddProjectRequest request,
                                                 @RequestHeader("Authorization") String token) {
        Optional<Member> member = jwtMember.getMember(token);
        if (member.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (member.get().getRole().toString().equals("ADMIN")) {
            Project updatedProject = projectService.update(id, request);
            return ResponseEntity.status(HttpStatus.OK).body(updatedProject);
        }

        Optional<ProjectMember> projectMember = projectMemberService
                .findByProjectAndMember(projectService.findById(id).get(), member.get());
        if (projectMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Project updatedProject = projectService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Project> deleteProject(@PathVariable("id") Integer id,
                                                 @RequestHeader("Authorization") String token) {
        Optional<Member> member = jwtMember.getMember(token);
        if (member.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (member.get().getRole().toString().equals("ADMIN")) {
            return projectService.deleteProject(id);
        }

        Optional<ProjectMember> projectMember = projectMemberService
                .findByProjectAndMember(projectService.findById(id).get(), member.get());
        if (projectMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return projectService.deleteProject(id);
    }
}

