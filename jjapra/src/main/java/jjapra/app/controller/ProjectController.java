package jjapra.app.controller;

import jjapra.app.config.jwt.JwtMember;
import jjapra.app.dto.project.AddProjectMemberRequest;
import jjapra.app.dto.project.AddProjectRequest;
import jjapra.app.model.issue.Issue;
import jjapra.app.model.member.Member;
import jjapra.app.model.member.MemberRole;
import jjapra.app.model.member.Role;
import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.response.GetProjectResponse;
import jjapra.app.response.Pairs;
import jjapra.app.response.ProjectDetailsResponse;
import jjapra.app.service.IssueService;
import jjapra.app.service.MemberService;
import jjapra.app.service.ProjectMemberService;
import jjapra.app.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
    private final IssueService issueService;

    @GetMapping("")
    public ResponseEntity<List<GetProjectResponse>> getProjects(@RequestHeader("Authorization") String token) {
        Optional<Member> loggedInUser = jwtMember.getMember(token);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (loggedInUser.get().getRole().toString().equals("ADMIN")) {
            List<Project> projects = projectService.findAll();
            List<GetProjectResponse> response = projects.stream().map(project -> {
                List<ProjectMember> projectMembers = projectMemberService.findByProject(project);
                List<String> members = projectMembers.stream().map(pm -> pm.getMember().getId()).toList();
                return new GetProjectResponse(project, Role.valueOf("ADMIN"), members);
            }).toList();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        
        List<ProjectMember> projectMembers = projectMemberService.findByMemberId(loggedInUser.get().getId());
        List<GetProjectResponse> response = projectMembers.stream().map(projectMember -> {
            Project project = projectMember.getProject();
            List<ProjectMember> projectMemberList = projectMemberService.findByProject(project);
            List<String> members = projectMemberList.stream().map(pm -> pm.getMember().getId()).toList();
            return new GetProjectResponse(project, projectMember.getRole(), members);
        }).toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
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
    public ResponseEntity<ProjectDetailsResponse> getProject(@PathVariable("id") Integer id, @RequestHeader("Authorization") String token) {
        Optional<Member> loggedInUser = jwtMember.getMember(token);
        if (loggedInUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<ProjectMember> projectMember = projectMemberService
                .findByProjectAndMember(projectService.findById(id).get(), loggedInUser.get());
        if (projectMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<ProjectMember> pmList = projectMemberService.findByProject(projectService.findById(id).get());
        List<Pairs<String, String>> memberRoles = pmList.stream().map(pm ->
                new Pairs<>(pm.getMember().getId(), pm.getRole().toString())).toList();
        List<Issue> issues = issueService.findByProjectId(id);
        ProjectDetailsResponse response = new ProjectDetailsResponse(
                projectService.findById(id).get(), issues, memberRoles);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") Integer id, @RequestBody AddProjectRequest request,
                                                 @RequestHeader("Authorization") String token) {
        Optional<Member> member = jwtMember.getMember(token);
        if (member.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (member.get().getRole().toString().equals("ADMIN")) {
            Optional<Project> updatedProject = Optional.ofNullable(projectService.update(id, request));
            if (updatedProject.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(updatedProject.get());
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
            issueService.findCommentByProjectId(id).forEach(issueService::deleteComment);
            issueService.findByProjectId(id).forEach(issueService::deleteIssue);
            projectMemberService.findByProject(projectService.findById(id).get()).forEach(projectMemberService::delete);
            projectService.deleteProject(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}

