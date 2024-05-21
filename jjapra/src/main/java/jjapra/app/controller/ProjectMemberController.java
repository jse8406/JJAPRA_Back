package jjapra.app.controller;

import jjapra.app.dto.AddProjectMemberRequest;
import jjapra.app.model.Member;
import jjapra.app.model.Project;
import jjapra.app.model.ProjectMember;
import jjapra.app.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
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
