package jjapra.app.controller;

import jjapra.app.dto.project.AddProjectMemberRequest;
import jjapra.app.model.member.Member;
import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@RequiredArgsConstructor
@RestController
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;

    @PostMapping("/projects/{id}")
    public ProjectMember save(@PathVariable("id") Integer id, @RequestBody AddProjectMemberRequest request) {
        Project project = projectMemberService.findProjectById(id);
        if (project == null) {
            return null;
        }
        Member member = projectMemberService.findMemberById(request.getMemberId());
        if (member == null) {
            return null;
        }

        return projectMemberService.save(request, project, member);
    }
}
