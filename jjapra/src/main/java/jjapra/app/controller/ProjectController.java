package jjapra.app.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jjapra.app.config.jwt.JwtProvider;
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

import static jjapra.app.config.jwt.JwtProvider.extractToken;

@EnableWebMvc
@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final JwtProvider jwtProvider;
    private final ProjectMemberService projectMemberService;
    private final MemberService memberService;

    @GetMapping("")
    public ResponseEntity<List<Project>> getProjects() {
        List<Project> projects = projectService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }

    @PostMapping("")
    public ResponseEntity<?> addProject(@RequestBody AddProjectRequest request, HttpServletRequest Httprequest) {
        if (request.getTitle().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Project project = projectService.findByTitle(request.getTitle());
        if (project != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Project savedProject = projectService.save(request);
        String token = extractToken(Httprequest);
        String username = jwtProvider.getUsername(token);
        Optional<Member> member = memberService.findByUsername(username);
        ProjectMember entity = AddProjectMemberRequest.toEntity(savedProject, member.get());
        projectMemberService.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable("id") Integer id) {
        Project project = projectService.findById(id);
        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") Integer id, @RequestBody AddProjectRequest request) {
        Project updatedProject = projectService.update(id, request);
        if (updatedProject == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Project> deleteProject(@PathVariable("id") Integer id) {
        return projectService.deleteProject(id);
    }
}

